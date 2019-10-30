package com.org.fplab.liveinfostream

import cats.effect._
import cats.effect.std.Queue
import cats.implicits._
import cats.mtl.ApplicativeAsk
import com.org.fplab.liveinfostream.betfair.betting.BettingClient
import com.org.fplab.liveinfostream.betfair.navigation.NavigationStream
import com.org.fplab.liveinfostream.betfair.subscription.models.LocalMarket
import com.org.fplab.liveinfostream.betfair.subscription.{MarketSubscription, NativeBetfairSubscription}
import com.org.fplab.liveinfostream.config.AppConfiguration
import com.org.fplab.liveinfostream.metrics.Prometheus
import com.org.fplab.liveinfostream.state.ApplicationState
import com.org.fplab.liveinfostream.webservice.WebRouter
import fs2._
import fs2.concurrent._

object Server extends IOApp {

  def run(args: List[String]): IO[ExitCode] =
    for {
      config <- AppConfiguration.getConfiguration[IO]

      // With interrupter we can signal to stop a stream
      interrupter <- SignallingRef[IO, Boolean](false)

      // NativeBetfairSubscription will fill this queue, but MarketSubscription will read it
      marketChangeQueue <- Queue.bounded[IO, LocalMarket](config.betfair.marketChangeQueueSize)
      emptyState        <- ApplicationState.empty[IO]
      applicationState  <- Ref.of[IO, ApplicationState[IO]](emptyState)
      _                 <- {
        implicit val configAsk: ApplicativeAsk[IO, AppConfiguration] =
          ApplicativeAsk.const[IO, AppConfiguration](config)

        Prometheus.make[IO].use { prometheus =>
          NativeBetfairSubscription
            .make[IO] {
              implicit val runtime = cats.effect.unsafe.IORuntime.global
              // This will be fired by Java betfair library in its own thread, that we do not control. So we need run IO here
              MarketSubscription.onExternalMessage(_, marketChangeQueue, applicationState, prometheus).unsafeRunSync()
            }
            .use(betfairSubscription =>
              mainProcess(interrupter, betfairSubscription, marketChangeQueue, applicationState, prometheus.metrics)
                .guarantee(interrupter.set(true))
            )
        }
      }
    } yield ExitCode.Success

  private def mainProcess[F[_]: Async](
    interrupter: SignallingRef[F, Boolean],
    subscription: NativeBetfairSubscription,
    marketChangeQueue: Queue[F, LocalMarket],
    applicationState: Ref[F, ApplicationState[F]],
    metrics: F[String]
  )(implicit
    C: ConfigurationAsk[F]
  ): F[Unit] =
    for {
      // Topic with JSON messages to be sent to client
      topic <- Topic[F, Option[String]]

      sessionIdReader  = Sync[F].delay(subscription.getSessionId)
      // Web server API (including web sockets)
      webServerStream <- WebRouter.createWebServerQueue(interrupter, applicationState, topic, metrics)
      _               <- Stream(
                           // Betfair navigation info (refreshed every hour)
                           NavigationStream.createNavigationAutoUpdateStream(interrupter, applicationState, sessionIdReader),
                           // Betfair runner info (refreshed every hour)
                           BettingClient.createBettingAutoUpdateStream(interrupter, applicationState, sessionIdReader),
                           // Reads market queue, identifies changed state and puts it into topic
                           MarketSubscription.createMarketChangeQueueProcessorStream(
                             interrupter,
                             applicationState,
                             marketChangeQueue,
                             topic
                           ),
                           webServerStream
                         ).parJoinUnbounded.compile.drain
    } yield ()
}
