package com.org.fplab.liveinfostream.config

import cats.effect.Blocker
import cats.implicits._
import ciris._

/** Betfair configuration */
final case class BetfairConfiguration(appKey: Secret[String],
                                      username: Secret[String],
                                      password: Secret[String],
                                      navigationUri: String = "https://api.betfair.com/exchange/betting/rest/v1/en/navigation/menu.json",
                                      bettingUri: String = "https://api.betfair.com/exchange/betting/json-rpc/v1",
                                      marketChangeQueueSize: Int = 500
                                     )

object BetfairConfiguration {
  def getConfiguration[F[_]](implicit blocker: Blocker): ConfigValue[BetfairConfiguration] = {
    (
      SecretFileConfigEntry.envSecretFile("BETFAIR_APP_KEY_FILE").or(env("BETFAIR_APP_KEY").as[String]).secret,
      SecretFileConfigEntry.envSecretFile("BETFAIR_USERNAME_FILE").or(env("BETFAIR_USERNAME").as[String]).secret,
      SecretFileConfigEntry.envSecretFile("BETFAIR_PASSWORD_FILE").or(env("BETFAIR_PASSWORD").as[String]).secret
      ).parMapN(BetfairConfiguration(_, _, _))
  }
}