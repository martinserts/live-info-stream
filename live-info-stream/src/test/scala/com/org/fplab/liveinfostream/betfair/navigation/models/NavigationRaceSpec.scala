package com.org.fplab.liveinfostream.betfair.navigation.models

import com.org.fplab.liveinfostream.UnitSpec
import io.circe.parser.decode

import NavigationRaceCodec._

class NavigationRaceSpec extends UnitSpec {
  val json =
    """
      |{
      |    "id": "27247020.1115",
      |    "name": "1300m 3yo",
      |    "startTime": "2014-08-12T11:15:00.000Z",
      |    "type": "RACE",
      |    "venue": "Deauville",
      |    "raceNumber" : "R1",
      |    "countryCode": "GB",
      |    "type": "RACE",
      |    "children": [
      |        {
      |          "exchangeId": "1",
      |          "id": "1.114881860",
      |          "marketStartTime": "2014-08-14T00:00:00.000Z",
      |          "marketType": "WIN",
      |          "numberOfWinners": "2",
      |          "name": "Over/Under 6.5 Goals",
      |          "type": "MARKET"
      |        }
      |    ]
      |}
      |""".stripMargin

  val jsonWrongType =
    """
      |{
      |    "id": "27247020.1115",
      |    "name": "1300m 3yo",
      |    "startTime": "2014-08-12T11:15:00.000Z",
      |    "type": "RACE",
      |    "venue": "Deauville",
      |    "raceNumber" : "R1",
      |    "countryCode": "GB",
      |    "type": "WRONG_RACE",
      |    "children": [
      |        {
      |          "exchangeId": "1",
      |          "id": "1.114881860",
      |          "marketStartTime": "2014-08-14T00:00:00.000Z",
      |          "marketType": "WIN",
      |          "numberOfWinners": "2",
      |          "name": "Over/Under 6.5 Goals",
      |          "type": "MARKET"
      |        }
      |    ]
      |}
      |""".stripMargin

  "circe" can "decode json" in {
    val result = decode[NavigationRace](json)
    result should be (Symbol("right"))

    assertResult(Right("27247020.1115"))(result.map(_.id))
    assertResult(Right(true))(result.map(_.children.isDefined))
    assertResult(Right(1))(result.map(_.children.get.length))
    assertResult(Right("1.114881860"))(result.map(_.children.get.head.id))
  }

  "circe" should "respect type" in {
    val result = decode[NavigationRace](jsonWrongType)
    result should be (Symbol("left"))
  }
}
