package com.org.fplab.liveinfostream.betfair.navigation.models

import com.org.fplab.liveinfostream.UnitSpec
import io.circe.parser.decode

import NavigationEventTypeChildCodec._

class NavigationEventTypeChildSpec extends UnitSpec {
  val jsonEventChild =
    """
      |{
      |    "id": "27244118",
      |    "name": "South Korea U20 (W) v Mexico U20 (W)",
      |    "countryCode": "GB",
      |    "type": "EVENT"
      |}
      |""".stripMargin

  "circe" can "decode Event child" in {
    val result = decode[NavigationEventTypeChild](jsonEventChild)
    result should be (Symbol("right"))

    val isCorrectType = result match {
      case Right(NetcGroup(g)) => g.`type` == "EVENT"
      case _ => false
    }
    assert(isCorrectType)
  }
}
