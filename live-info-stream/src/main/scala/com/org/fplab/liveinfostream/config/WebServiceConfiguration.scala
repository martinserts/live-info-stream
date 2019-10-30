package com.org.fplab.liveinfostream.config

import cats._
import cats.effect._
import ciris._
import ciris.cats.effect._

/** Web service configuration */
final case class WebServiceConfiguration(
                                        useCors: Boolean,
                                        port: Int,
                                        staticFilesRoot: String
                                        )

object WebServiceConfiguration {
  def getConfiguration[F[_]: Sync](implicit M: MonadError[F, Throwable]): F[WebServiceConfiguration] = {
    val config = loadConfig(
      envF[F, Boolean]("WEBSERVICE_USE_CORS").orValue(false),
      envF[F, Int]("WEBSERVICE_PORT"),
      envF[F, String]("WEBSERVICE_ROOT")
    ) { WebServiceConfiguration(_, _, _) }

    config.orRaiseThrowable
  }
}

