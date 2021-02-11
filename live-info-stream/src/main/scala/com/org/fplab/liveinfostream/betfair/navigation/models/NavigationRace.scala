package com.org.fplab.liveinfostream.betfair.navigation.models

import java.time.ZonedDateTime

import com.org.fplab.liveinfostream.betfair.navigation.models.NavigationMarketCodec._
import io.circe.Decoder

final case class NavigationRace(
  id: String,
  name: String,
  startTime: Option[ZonedDateTime],
  venue: Option[String],
  raceNumber: Option[String],
  countryCode: Option[String],
  children: Option[List[NavigationMarket]],
  `type`: String
)

object NavigationRaceCodec {
  implicit lazy val decodeNavigationRace: Decoder[NavigationRace] =
    Decoder
      .forProduct8(
        "id",
        "name",
        "startTime",
        "venue",
        "raceNumber",
        "countryCode",
        "children",
        "type"
      )(NavigationRace.apply)
      .ensure(_.`type` == "RACE", "Type must be RACE")
}
