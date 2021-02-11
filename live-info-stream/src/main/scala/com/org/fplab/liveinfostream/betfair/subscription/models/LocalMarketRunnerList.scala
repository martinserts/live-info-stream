package com.org.fplab.liveinfostream.betfair.subscription.models

import com.betfair.esa.client.cache.market.MarketRunnerSnap

import scala.jdk.CollectionConverters._

object LocalMarketRunnerList {
  def fromMarketRunnerList(items: java.util.List[MarketRunnerSnap]): LocalMarketRunnerList =
    LocalMarketRunnerList(
      items.asScala.toList.map(LocalMarketRunner.fromMarketRunner(_))
    )
}

final case class LocalMarketRunnerList(items: List[LocalMarketRunner])
