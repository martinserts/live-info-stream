package com.org.fplab.liveinfostream.webservice.controllers

import com.org.fplab.liveinfostream.betfair.betting.state.BettingState
import com.org.fplab.liveinfostream.betfair.navigation.state.NavigationState
import com.org.fplab.liveinfostream.betfair.subscription.models.LocalMarket
import com.org.fplab.liveinfostream.state.ApplicationState
import com.org.fplab.liveinfostream.webservice.core.MarketConverter
import com.org.fplab.liveinfostream.webservice.models.GuiMarket

/** Market list API */
object MarketListController {
  /** Market list fetched initially by client. All subsequent updates are via web socket messages */
  def getMarketList(state: ApplicationState): List[GuiMarket] = {
    val navState = state.navigation
    val marketNameResolver = getMarketName(navState)(_)
    val eventNameResolver = getEventName(navState)(_)
    val runnerNameResolver = getRunnerName(state.betting)(_)

    state.marketSubscription.markets.values
      .filter(isMarketVisible)
      .map(MarketConverter.toGuiMarket(marketNameResolver, eventNameResolver, runnerNameResolver))
      .toList
  }

  /** True, if market should be shown */
  private def isMarketVisible(market: LocalMarket): Boolean = {
    val md = market.marketDefinition
    md.turnInPlayEnabled && md.bettingType == "ODDS" && md.marketType == "WIN" && md.status != "CLOSED"
  }

  /** Fetches market name from navigation data */
  private def getMarketName(navState: NavigationState)(marketId: String): String =
    navState.markets.getOrElse(marketId, "Unknown market")

  /** Fetches event name from navigation data */
  private def getEventName(navState: NavigationState)(eventId: String): String =
    navState.events.getOrElse(eventId, "Unknown event")

  /** Fetches runner name from betting data */
  private def getRunnerName(bettingState: BettingState)(runnerId: Long): String =
    bettingState.runners.getOrElse(runnerId, "Unknown runner")
}
