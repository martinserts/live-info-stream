package com.org.fplab.liveinfostream.betfair.navigation.models

import com.org.fplab.liveinfostream.UnitSpec
import io.circe.parser.decode

import NavigationGroupCodec._

class NavigationGroupSpec extends UnitSpec {
  val json =
    """
      |{
      |    "id": "74568202414",
      |    "name": "Womens Soccer",
      |    "type": "GROUP"
      |}
      |""".stripMargin

  "circe" can "decode json" in {
    val result = decode[NavigationGroup](json)
    result should be (Symbol("right"))

//    assertResult(Right("1.114881860"))(result.map(_.id))
  }
}
