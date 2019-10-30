package com.org.fplab.liveinfostream.webservice

import cats.effect._
import cats.implicits._
import com.org.fplab.liveinfostream.ConfigurationAsk
import com.org.fplab.liveinfostream.state.ApplicationState
import fs2._
import fs2.concurrent._
import org.http4s.blaze.server._
import org.http4s.server.Router
import org.http4s.server.middleware.CORS
import org.http4s.server.staticcontent._

object WebRouter {

  /** Creates web server. It will enable CORS in development mode */
  def createWebServerQueue[F[_]: Async](
    interrupter: SignallingRef[F, Boolean],
    stateRef: Ref[F, ApplicationState[F]],
    topic: Topic[F, Option[String]],
    metrics: F[String]
  )(implicit
    C: ConfigurationAsk[F]
  ): F[Stream[F, ExitCode]] =
    for {
      config <- C.reader(_.webService)
      server  = BlazeServerBuilder[F]
                  .bindHttp(config.port, "0.0.0.0")
                  .withHttpWebSocketApp { wsBuilder =>
                    val staticFiles = fileService(FileService.Config[F](config.staticFilesRoot))
                    val router      = Router(
                      "/api" -> new ApiEndpoints(wsBuilder, stateRef, topic, metrics).eventsEndpoint,
                      ""     -> staticFiles
                    ).orNotFound
                    if (config.useCors) CORS.policy.withAllowOriginAll(router) else router
                  }
                  .serve
                  .interruptWhen(interrupter)
    } yield server
}
