package com.org.fplab.liveinfostream.config

import cats.implicits._
import ciris._

/** Web service configuration */
final case class WebServiceConfiguration(
                                        useCors: Boolean,
                                        port: Int,
                                        staticFilesRoot: String
                                        )

object WebServiceConfiguration {
  def getConfiguration[F[_]]: ConfigValue[WebServiceConfiguration] = {
    (
      env("WEBSERVICE_USE_CORS").as[Boolean].or(ConfigValue.default(false)),
      env("WEBSERVICE_PORT").as[Int],
      env("WEBSERVICE_ROOT").as[String]
    ).parMapN(WebServiceConfiguration(_, _, _))
  }
}

