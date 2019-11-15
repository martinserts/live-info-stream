package com.org.fplab.liveinfostream

import cats.implicits._
import cats.effect._
import cats.effect.concurrent._
import cats.mtl.ApplicativeAsk
import com.org.fplab.liveinfostream.betfair.betting.BettingClient
import com.org.fplab.liveinfostream.betfair.navigation.NavigationStream
import com.org.fplab.liveinfostream.betfair.subscription.models.LocalMarket
import com.org.fplab.liveinfostream.betfair.subscription.{MarketSubscription, NativeBetfairSubscription}
import com.org.fplab.liveinfostream.config.AppConfiguration
import com.org.fplab.liveinfostream.state.ApplicationState
import com.org.fplab.liveinfostream.webservice.WebRouter
import fs2._
import fs2.concurrent._
import org.log4s.getLogger

object Server extends IOApp {
  private[this] val logger = getLogger

  def run(args: List[String]): IO[ExitCode] = for {
    config <- Blocker[IO].use { blocker => AppConfiguration.getConfiguration[IO](blocker) }

    // With interrupter we can signal to stop a stream
    interrupter <- SignallingRef[IO, Boolean](false)
    // We will flag interrupter when stopping service
    signalStop = interrupter.set(true)

    // NativeBetfairSubscription will fill this queue, but MarketSubscription will read it
    marketChangeQueue <- Queue.bounded[IO, LocalMarket](config.betfair.marketChangeQueueSize)
    _ <- {
      implicit val configAsk = ApplicativeAsk.const[IO, AppConfiguration](config)
      val betfair = NativeBetfairSubscription.createResource[IO](
        // This will be fired by Java betfair library in its own thread, that we do not control. So we need run IO here
        MarketSubscription.onExternalMessage(_, marketChangeQueue).unsafeRunSync
      )
      betfair.use (betfairSubscription => for {
          fiber <- mainProcess(interrupter, betfairSubscription, marketChangeQueue).start
          // Service will be stopped with SIGINT, so we need to notify streams that they need to stop
          result <- fiber.join.guarantee(signalStop)
        } yield result
      )
    }
  } yield ExitCode.Success

  private def mainProcess[F[_]: Sync : Concurrent : ConcurrentEffect : Timer : ContextShift](
                                                                               interrupter: SignallingRef[F, Boolean],
                                                                               subscription: NativeBetfairSubscription,
                                                                               marketChangeQueue: Queue[F, LocalMarket])
                                                                             (implicit C: ConfigurationAsk[F]): F[Unit] = for {
    emptyState <- ApplicationState.empty
    applicationState <- Ref.of[F, ApplicationState[F]](emptyState)
    // Topic with JSON messages to be sent to client
    topic <- Topic[F, Option[String]](None)

    sessionId = subscription.getSessionId
    // Web server API (including web sockets)
    webServerStream <- WebRouter.createWebServerQueue(interrupter, applicationState, topic)
    _ <- Stream(
      // Betfair navigation info (refreshed every hour)
      NavigationStream.createNavigationAutoUpdateStream(interrupter, applicationState, sessionId),
      // Betfair runner info (refreshed every hour)
      BettingClient.createBettingAutoUpdateStream(interrupter, applicationState, sessionId),
      // Reads market queue, identifies changed state and puts it into topic
      MarketSubscription.createMarketChangeQueueProcessorStream(interrupter, applicationState, marketChangeQueue, topic),
      webServerStream
    ).parJoinUnbounded.compile.drain
  } yield ()
}
