package com.org.fplab.liveinfostream.webservice.controllers

import cats._
import cats.effect.Clock
import cats.implicits._
import com.org.fplab.liveinfostream.betfair.betting.state.BettingState
import com.org.fplab.liveinfostream.betfair.navigation.state.NavigationState
import com.org.fplab.liveinfostream.betfair.subscription.models.LocalMarket
import com.org.fplab.liveinfostream.state.ApplicationState
import com.org.fplab.liveinfostream.webservice.core.MarketConverter
import com.org.fplab.liveinfostream.webservice.models.GuiMarket

import java.time.Instant

/** Market list API */
object MarketListController {

  /** Market list fetched initially by client. All subsequent updates are via web socket messages */
  def marketList[F[_]: Functor: Clock](state: ApplicationState[F]): F[List[GuiMarket]] =
    Clock[F].realTimeInstant.map { now =>
      val staleInstant = now.minusSeconds(60 * 60)
      val navState     = state.navigation
      state.marketSubscription.markets.values
        .filter(m => marketVisible(m) && !marketStale(staleInstant, m))
        .flatMap(MarketConverter.toGuiMarket(marketName(navState), eventName(navState), runnerName(state.betting)))
        .toList
    }

  /** True, if market should be shown */
  private def marketVisible(market: LocalMarket): Boolean = {
    val md = market.marketDefinition
    md.turnInPlayEnabled && md.bettingType == "ODDS" && md.marketType == "WIN" && md.status != "CLOSED"
  }

  private def marketStale(staleInstant: Instant, market: LocalMarket): Boolean =
    market.marketDefinition.marketTime.compareTo(staleInstant) < 0

  /** Fetches market name from navigation data */
  private def marketName(navState: NavigationState): String => Option[String] = { marketId =>
    Some(navState.markets.getOrElse(marketId, "Unknown market"))
  }

  /** Fetches event name from navigation data */
  private def eventName(navState: NavigationState): String => Option[String] = { eventId =>
    Some(navState.events.getOrElse(eventId, "Unknown event"))
  }

  /** Fetches runner name from betting data */
  private def runnerName(bettingState: BettingState): Long => Option[String] = { runnerId =>
    Some(bettingState.runners.getOrElse(runnerId, "Unknown runner"))
  }
}
