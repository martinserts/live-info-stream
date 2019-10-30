package com.org.fplab.liveinfostream.webservice

import java.util.concurrent.Executors

import cats.effect._
import cats.effect.concurrent.Ref
import cats.implicits._
import com.org.fplab.liveinfostream.ConfigurationAsk
import com.org.fplab.liveinfostream.state.ApplicationState
import fs2._
import fs2.concurrent._
import org.http4s.server.Router
import org.http4s.server.blaze._
import org.http4s.server.middleware.CORS
import org.http4s.server.staticcontent._
import org.http4s.syntax.kleisli._

import scala.concurrent.ExecutionContext

object WebRouter {
  /** Creates web server. It will enable CORS in development mode */
  def createWebServerQueue[F[_]: Sync : Timer : ConcurrentEffect : ContextShift](
                                                                   interrupter: SignallingRef[F, Boolean],
                                                                   stateRef: Ref[F, ApplicationState],
                                                                   topic: Topic[F, Option[String]]
                                                                 )(implicit C: ConfigurationAsk[F]): F[Stream[F, ExitCode]] =  for {
    config <- C.reader(_.webService)

    blockingEc = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(4))
    blocker = Blocker.liftExecutionContext(blockingEc)
    staticFiles = fileService(FileService.Config[F](config.staticFilesRoot, blocker))

    router = Router(
      "/api" -> new ApiEndpoints(stateRef, topic).eventsEndpoint,
      "" -> staticFiles
    )
    app = router.orNotFound
    wrappedApp = if (config.useCors) CORS(app) else app

    server = BlazeServerBuilder[F]
      .bindHttp(config.port, "localhost")
      .withHttpApp(wrappedApp)
      .serve
      .interruptWhen(interrupter)
  } yield server
}
