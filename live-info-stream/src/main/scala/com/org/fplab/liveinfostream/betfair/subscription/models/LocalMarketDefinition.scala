package com.org.fplab.liveinfostream.betfair.subscription.models

import java.time.Instant

import com.betfair.esa.swagger.model.MarketDefinition
import com.org.fplab.liveinfostream.utils.DateTimeUtils.threeTenToInstant

object LocalMarketDefinition {
  def fromMarketDefinition(marketDefinition: MarketDefinition): LocalMarketDefinition = {
    LocalMarketDefinition(
      marketDefinition.getEventId,
      marketDefinition.getEventTypeId,
      marketDefinition.getMarketType,
      marketDefinition.getBettingType.getValue,
      marketDefinition.isInPlay,
      marketDefinition.isTurnInPlayEnabled,
      threeTenToInstant(marketDefinition.getMarketTime),
      marketDefinition.getStatus.getValue,
      LocalRunnerDefinitionList.fromRunnerDefinitionList(marketDefinition.getRunners)
    )
  }
}

case class LocalMarketDefinition(
                                  eventId: String,
                                  eventTypeId: String,
                                  marketType: String,
                                  bettingType: String,
                                  inPlay: Boolean,
                                  turnInPlayEnabled: Boolean,
                                  marketTime: Instant,
                                  status: String,
                                  runners: LocalRunnerDefinitionList
                                )
