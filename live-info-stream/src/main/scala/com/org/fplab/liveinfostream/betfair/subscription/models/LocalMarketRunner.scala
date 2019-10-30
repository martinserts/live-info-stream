package com.org.fplab.liveinfostream.betfair.subscription.models

import com.betfair.esa.client.cache.market.MarketRunnerSnap

object LocalMarketRunner {
  def fromMarketRunner(marketRunner: MarketRunnerSnap): LocalMarketRunner =
    LocalMarketRunner(
      marketRunner.getRunnerId.getSelectionId,
      marketRunner.getRunnerId.getHandicap,
      LocalLevelPriceSizeList.fromLevelPriceSizeList(marketRunner.getPrices.getBdatb),
      LocalLevelPriceSizeList.fromLevelPriceSizeList(marketRunner.getPrices.getBdatl)
    )
}

case class LocalMarketRunner(
                            selectionId: Long,
                            hc: Double,
                            bestAvailableToBet: LocalLevelPriceSizeList,
                            bestAvailableToLay: LocalLevelPriceSizeList
                            )
