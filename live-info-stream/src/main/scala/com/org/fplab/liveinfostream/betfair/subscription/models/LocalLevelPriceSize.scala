package com.org.fplab.liveinfostream.betfair.subscription.models

import com.betfair.esa.client.cache.util.LevelPriceSize

object LocalLevelPriceSize {
  def fromLevelPriceSize(levelPriceSize: LevelPriceSize): LocalLevelPriceSize =
    LocalLevelPriceSize(
      levelPriceSize.getLevel,
      levelPriceSize.getPrice,
      levelPriceSize.getSize
    )
}

final case class LocalLevelPriceSize(
  level: Int,
  price: Double,
  size: Double
)
