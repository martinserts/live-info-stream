package com.org.fplab.liveinfostream.betfair.betting.models

case class MarketFilter(
                         eventTypeIds: List[String],
                         turnInPlayEnabled: Boolean,
                         marketBettingTypes: List[String]
                       )
