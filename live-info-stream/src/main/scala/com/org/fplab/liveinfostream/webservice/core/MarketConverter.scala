package com.org.fplab.liveinfostream.webservice.core

import cats.implicits._
import com.org.fplab.liveinfostream.betfair.subscription.models.{
  LocalMarket,
  LocalMarketRunner,
  LocalRunnerDefinitionList
}
import com.org.fplab.liveinfostream.utils.DoubleUtils._
import com.org.fplab.liveinfostream.webservice.models.{GuiEvent, GuiMarket, GuiRunner}

object MarketConverter {

  /** Converts LocalMarket to GuiMarket and augments with missing data */
  def toGuiMarket(
    marketNameResolver: String => Option[String],
    eventNameResolver: String => Option[String],
    runnerNameResolver: Long => Option[String]
  )(
    market: LocalMarket
  ): Option[GuiMarket] = {
    val md = market.marketDefinition
    (marketNameResolver(market.marketId), eventNameResolver(md.eventId)).mapN {
      case (marketName, eventName) =>
        GuiMarket(
          id = market.marketId,
          name = marketName,
          inPlay = md.inPlay,
          marketTime = md.marketTime,
          status = md.status,
          event = GuiEvent(md.eventId, eventName),
          runners = market.runners.items
            .traverse(toGuiRunner(runnerNameResolver, market.marketDefinition.runners))
            .getOrElse(List.empty),
          tradedVolume = market.tradedVolume
        )
    }
  }

  /** Converts LocalMarketRunner to GuiRunner */
  private def toGuiRunner(
    runnerNameResolver: Long => Option[String],
    runnerDefinitions: LocalRunnerDefinitionList
  )(
    localRunner: LocalMarketRunner
  ): Option[GuiRunner] = {
    val DefaultPriceIfMissing: Double = 0

    val status = runnerDefinitions.items
      .find(r => r.id == localRunner.selectionId && (r.hc ~== localRunner.hc))
      .map(r => r.status)
      .getOrElse("")

    runnerNameResolver(localRunner.selectionId).map { runnerName =>
      GuiRunner(
        id = localRunner.selectionId,
        hc = localRunner.hc,
        name = runnerName,
        price = localRunner.bestAvailableToBet.items.find(_.level == 1).map(_.price).getOrElse(DefaultPriceIfMissing),
        status = status,
        volume = localRunner.totalVolume
      )
    }
  }
}
