package com.org.fplab.liveinfostream.betfair.subscription.models

import com.betfair.esa.client.cache.market.MarketSnap

object LocalMarket {
  def fromMarketSnap(marketSnap: MarketSnap): LocalMarket = {
    LocalMarket(
      marketSnap.getMarketId,
      LocalMarketDefinition.fromMarketDefinition(marketSnap.getMarketDefinition),
      LocalMarketRunnerList.fromMarketRunnerList(marketSnap.getMarketRunners),
      marketSnap.getTradedVolume
    )
  }
}

case class LocalMarket(
                      marketId: String,
                      marketDefinition: LocalMarketDefinition,
                      runners: LocalMarketRunnerList,
                      tradedVolume: Double
                      )
