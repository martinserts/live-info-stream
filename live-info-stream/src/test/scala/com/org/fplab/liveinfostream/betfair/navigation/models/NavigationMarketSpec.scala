package com.org.fplab.liveinfostream.betfair.navigation.models

import com.org.fplab.liveinfostream.UnitSpec
import io.circe.parser.decode

import NavigationMarketCodec._

class NavigationMarketSpec extends UnitSpec {
  val json =
    """
      |{
      |    "exchangeId": "1",
      |    "id": "1.114881860",
      |    "marketStartTime": "2014-08-14T00:00:00.000Z",
      |    "marketType": "WIN",
      |    "numberOfWinners": "2",
      |    "name": "Over/Under 6.5 Goals",
      |    "type": "MARKET"
      |}
      |""".stripMargin

  val jsonWrongType =
    """
      |{
      |    "exchangeId": "1",
      |    "id": "1.114881860",
      |    "marketStartTime": "2014-08-14T00:00:00.000Z",
      |    "marketType": "WIN",
      |    "numberOfWinners": "2",
      |    "name": "Over/Under 6.5 Goals",
      |    "type": "WRONG_MARKET"
      |}
      |""".stripMargin

  "circe" can "decode json" in {
    val result = decode[NavigationMarket](json)
    result should be (Symbol("right"))

    assertResult(Right("1.114881860"))(result.map(_.id))
  }

  "circe" should "respect type" in {
    val result = decode[NavigationMarket](jsonWrongType)
    result should be (Symbol("left"))
  }
}
