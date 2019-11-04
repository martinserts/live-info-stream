package com.org.fplab.liveinfostream.config

import cats.implicits._
import cats.effect._
import ciris._

/** Application configuration root */
final case class AppConfiguration(
                                 devEnvironment: Boolean, // true if running as development environment
                                 betfair: BetfairConfiguration, // Betfair specific config
                                 webService: WebServiceConfiguration // Web service configuration
                                 )

object AppConfiguration {
  def getConfiguration[F[_]: Async : ContextShift](blocker: Blocker): F[AppConfiguration] = {
    implicit val b: Blocker = blocker
    (
      env("DEV_ENVIRONMENT").as[Boolean].or(ConfigValue.default(false)),
      BetfairConfiguration.getConfiguration,
      WebServiceConfiguration.getConfiguration
    ).parMapN(AppConfiguration(_, _, _)).load[F]
  }
}
