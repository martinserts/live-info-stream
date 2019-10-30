package com.org.fplab.liveinfostream.config

import cats.implicits._
import ciris._
import sttp.client3._
import sttp.model.Uri

/** Betfair configuration */
final case class BetfairConfiguration(
  appKey: Secret[String],
  username: Secret[String],
  password: Secret[String],
  navigationUri: Uri = uri"https://api.betfair.com/exchange/betting/rest/v1/en/navigation/menu.json",
  bettingUri: Uri = uri"https://api.betfair.com/exchange/betting/json-rpc/v1",
  marketChangeQueueSize: Int = 500
)

object BetfairConfiguration {
  def getConfiguration[F[_]]: ConfigValue[Effect, BetfairConfiguration] =
    (
      (SecretFileConfigEntry.envSecretFile("BETFAIR_APP_KEY_FILE") or env("BETFAIR_APP_KEY")).as[String].secret,
      (SecretFileConfigEntry.envSecretFile("BETFAIR_USERNAME_FILE") or env("BETFAIR_USERNAME")).as[String].secret,
      (SecretFileConfigEntry.envSecretFile("BETFAIR_PASSWORD_FILE") or env("BETFAIR_PASSWORD")).as[String].secret
    ).mapN(BetfairConfiguration(_, _, _))
}
