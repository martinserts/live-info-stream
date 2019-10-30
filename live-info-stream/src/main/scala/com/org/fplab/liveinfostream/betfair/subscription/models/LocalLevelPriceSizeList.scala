package com.org.fplab.liveinfostream.betfair.subscription.models

import com.betfair.esa.client.cache.util.LevelPriceSize

import scala.jdk.CollectionConverters._

object LocalLevelPriceSizeList {
  def fromLevelPriceSizeList(items: java.util.List[LevelPriceSize]): LocalLevelPriceSizeList =
    LocalLevelPriceSizeList(
      items.asScala.toList.map(LocalLevelPriceSize.fromLevelPriceSize(_))
    )
}

case class LocalLevelPriceSizeList(items: List[LocalLevelPriceSize])
