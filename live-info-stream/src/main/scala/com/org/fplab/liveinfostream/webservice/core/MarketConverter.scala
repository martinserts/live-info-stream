package com.org.fplab.liveinfostream.webservice.core

import com.org.fplab.liveinfostream.betfair.subscription.models.{LocalMarket, LocalMarketRunner, LocalRunnerDefinitionList}
import com.org.fplab.liveinfostream.webservice.models.{GuiEvent, GuiMarket, GuiRunner}

object MarketConverter {
  /** Converts LocalMarket to GuiMarket and augments with missing data */
  def toGuiMarket(
                   marketNameResolver: String => String,
                   eventNameResolver: String => String,
                   runnerNameResolver: Long => String)
                 (market: LocalMarket): GuiMarket = {
    val md = market.marketDefinition
    GuiMarket(
      id = market.marketId,
      name = marketNameResolver(market.marketId),
      inPlay = md.inPlay,
      marketTime = md.marketTime,
      status = md.status,
      event = GuiEvent(md.eventId, eventNameResolver(md.eventId)),
      runners = market.runners.items.map(toGuiRunner(runnerNameResolver, market.marketDefinition.runners)),
      tradedVolume = market.tradedVolume
    )
  }

  /** Converts LocalMarketRunner to GuiRunner */
  private def toGuiRunner(runnerNameResolver: Long => String, runnerDefinitions: LocalRunnerDefinitionList)
                         (localRunner: LocalMarketRunner): GuiRunner = {
    val DefaultPriceIfMissing: Double = 0

    val status = runnerDefinitions.items
      .find(r => r.id == localRunner.selectionId && r.hc == localRunner.hc)
      .map(r => r.status)
      .getOrElse("")

    GuiRunner(
      id = localRunner.selectionId,
      hc = localRunner.hc,
      name = runnerNameResolver(localRunner.selectionId),
      price = localRunner.bestAvailableToBet.items.find(_.level == 1).map(_.price).getOrElse(DefaultPriceIfMissing),
      status = status,
      volume = localRunner.totalVolume
    )
  }
}
