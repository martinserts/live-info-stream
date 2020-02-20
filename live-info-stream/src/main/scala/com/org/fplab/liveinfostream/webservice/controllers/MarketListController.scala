package com.org.fplab.liveinfostream.webservice.controllers

import java.time.Instant

import cats._
import cats.implicits._
import cats.effect.Clock

import com.org.fplab.liveinfostream.betfair.betting.state.BettingState
import com.org.fplab.liveinfostream.betfair.navigation.state.NavigationState
import com.org.fplab.liveinfostream.betfair.subscription.models.LocalMarket
import com.org.fplab.liveinfostream.state.ApplicationState
import com.org.fplab.liveinfostream.webservice.core.MarketConverter
import com.org.fplab.liveinfostream.webservice.models.GuiMarket

import scala.concurrent.duration.{MILLISECONDS, HOURS}

/** Market list API */
object MarketListController {
  /** Market list fetched initially by client. All subsequent updates are via web socket messages */
  def getMarketList[F[_] : Functor](state: ApplicationState[F])
                         (clock: Clock[F]): F[List[GuiMarket]] = for {
    realTime <- clock.realTime(MILLISECONDS)
    staleInstant = Instant.ofEpochMilli(realTime).minusSeconds(60 * 60)

    navState = state.navigation
    marketNameResolver = getMarketName(navState)(_)
    eventNameResolver = getEventName(navState)(_)
    runnerNameResolver = getRunnerName(state.betting)(_)

    result = state.marketSubscription.markets.values
      .filter(m => isMarketVisible(m) && !isMarketStale(staleInstant)(m))
      .map(MarketConverter.toGuiMarket(marketNameResolver, eventNameResolver, runnerNameResolver))
      .toList
  } yield result

  /** True, if market should be shown */
  private def isMarketVisible(market: LocalMarket): Boolean = {
    val md = market.marketDefinition
    md.turnInPlayEnabled && md.bettingType == "ODDS" && md.marketType == "WIN" && md.status != "CLOSED"
  }

  private def isMarketStale(staleInstant: Instant)(market: LocalMarket): Boolean = {
    market.marketDefinition.marketTime.compareTo(staleInstant) < 0
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
