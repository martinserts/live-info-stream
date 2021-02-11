package com.org.fplab.liveinfostream.betfair.betting.models

final case class MarketFilter(
  eventTypeIds: List[String],
  turnInPlayEnabled: Boolean,
  marketBettingTypes: List[String]
)
