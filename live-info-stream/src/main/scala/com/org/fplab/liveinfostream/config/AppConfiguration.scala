package com.org.fplab.liveinfostream.config

import cats._
import cats.implicits._
import cats.effect._
import ciris._
import ciris.cats.effect._
import com.org.fplab.liveinfostream.ConfigurationAsk

/** Application configuration root */
final case class AppConfiguration(
                                 devEnvironment: Boolean, // true if running as development environment
                                 betfair: BetfairConfiguration, // Betfair specific config
                                 webService: WebServiceConfiguration // Web service configuration
                                 )

object AppConfiguration {
  def getConfiguration[F[_]: Sync](implicit M: MonadError[F, Throwable]): F[AppConfiguration] = {
    for {
      devEnvironmet <- envF[F, Boolean]("DEV_ENVIRONMENT").orValue(false).orRaiseThrowable
      betfair <- BetfairConfiguration.getConfiguration
      webService <- WebServiceConfiguration.getConfiguration
    } yield AppConfiguration(devEnvironmet, betfair, webService)
  }
}
