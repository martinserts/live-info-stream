package com.org.fplab.liveinfostream.betfair.navigation.models

import java.time.ZonedDateTime

import io.circe.Decoder

final case class NavigationMarket(
  id: String,
  name: String,
  exchangeId: Option[String],
  marketType: Option[String],
  marketStartTime: Option[ZonedDateTime],
  numberOfWinners: Option[Int],
  `type`: String
)

object NavigationMarketCodec {
  implicit lazy val decodeNavigationMarket: Decoder[NavigationMarket] =
    Decoder
      .forProduct7(
        "id",
        "name",
        "exchangeId",
        "marketType",
        "marketStartTime",
        "numberOfWinnders",
        "type"
      )(NavigationMarket.apply)
      .ensure(_.`type` == "MARKET", "Type must be MARKET")
}
