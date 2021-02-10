package com.org.fplab.liveinfostream.betfair.betting.models

import com.org.fplab.liveinfostream.UnitSpec
import com.org.fplab.liveinfostream.betfair.betting.BettingClient

class ListMarketCatalagueRequestSpec extends UnitSpec {
  "circe" can "generate json" in {
    val request: ListMarketCatalogueRequest = ListMarketCatalogueRequest(
      MarketFilter(eventTypeIds = List("7"), turnInPlayEnabled = true, marketBettingTypes = List("ODDS")),
      marketProjection = List("RUNNER_DESCRIPTION"),
      sort = "FIRST_TO_START",
      maxResults = 1000
    )

    val rpc: RpcRequest[ListMarketCatalogueRequest] = RpcRequest("listMarketCatalogue", request)

    val json = rpc.getJson
    assert(json.isObject)

    val idResult = for {
      o        <- json.asObject
      idJson   <- o("id")
      idNumber <- idJson.asNumber
      id       <- idNumber.toInt
    } yield id
    assertResult(Some(1))(idResult)

    val method = json.hcursor.get[String]("method")
    assertResult(Right("SportsAPING/v1.0/listMarketCatalogue"))(method)

    val sort = json.hcursor.downField("params").get[String]("sort")
    assertResult(Right("FIRST_TO_START"))(sort)
  }

  "circe" can "parse runners" in {
    val json: String =
      """
        |{
        |  "jsonrpc" : "2.0",
        |  "result" : [
        |    {
        |      "marketId" : "1.163998080",
        |      "marketName" : "3600m 4yo Claim Hrd",
        |      "totalMatched" : 2679.334056,
        |      "runners" : [
        |        {
        |          "selectionId" : 20397285,
        |          "runnerName" : "1. Catch All",
        |          "handicap" : 0.0,
        |          "sortPriority" : 1
        |        },
        |        {
        |          "selectionId" : 26231456,
        |          "runnerName" : "2. Kimoko",
        |          "handicap" : 0.0,
        |          "sortPriority" : 2
        |        }
        |      ]
        |    },
        |    {
        |      "marketId" : "1.163998080",
        |      "marketName" : "3600m 4yo Claim Hrd",
        |      "totalMatched" : 2679.334056,
        |      "runners" : [
        |        {
        |          "selectionId" : 70397285,
        |          "runnerName" : "4. Catch All",
        |          "handicap" : 0.0,
        |          "sortPriority" : 1
        |        },
        |        {
        |          "selectionId" : 76231456,
        |          "runnerName" : "5. Kimoko",
        |          "handicap" : 0.0,
        |          "sortPriority" : 2
        |        }
        |      ]
        |    }
        |  ]
        |}
        |""".stripMargin

    assertResult(4)(BettingClient.parseRunners(json).length)
  }
}
