package com.org.fplab.liveinfostream.config

import cats._
import cats.data._
import cats.effect._
import ciris.{Secret, envF, loadConfig}
import ciris.cats.effect._

/** Betfair configuration */
final case class BetfairConfiguration(appKey: Secret[String],
                                      username: Secret[String],
                                      password: Secret[String],
                                      navigationUri: String = "https://api.betfair.com/exchange/betting/rest/v1/en/navigation/menu.json",
                                      bettingUri: String = "https://api.betfair.com/exchange/betting/json-rpc/v1",
                                      marketChangeQueueSize: Int = 500
                                     )

object BetfairConfiguration {
  def getConfiguration[F[_]: Sync](implicit M: MonadError[F, Throwable]): F[BetfairConfiguration] = {
    val config = loadConfig(
      envF[F, Secret[String]]("BETFAIR_APP_KEY"),
      envF[F, Secret[String]]("BETFAIR_USERNAME"),
      envF[F, Secret[String]]("BETFAIR_PASSWORD")
    ) { (appKey, username, password) => BetfairConfiguration(appKey, username, password) }

    EitherT(config.result)
      .leftMap((err) => new Exception(err.message))
      .rethrowT
  }
}