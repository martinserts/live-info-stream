package com.org.fplab.liveinfostream.state

import com.org.fplab.liveinfostream.betfair.betting.state.BettingState
import com.org.fplab.liveinfostream.betfair.navigation.state.NavigationState
import com.org.fplab.liveinfostream.betfair.subscription.state.MarketSubscriptionState
import com.org.fplab.liveinfostream.webservice.state.WebServiceState
import monocle._
import monocle.macros.GenLens

/** The root of application state */
case class ApplicationState(
                             marketSubscription: MarketSubscriptionState,   // State of live stream data
                             navigation: NavigationState,                   // State of navigation data (refreshed hourly)
                             betting: BettingState,                         // State contains runner id/names
                             webService: WebServiceState                    // State contains online user count
                           )

object ApplicationState {
  /** Lens to MarketSubscriptionState */
  val marketSubscription: Lens[ApplicationState, MarketSubscriptionState] = GenLens[ApplicationState](_.marketSubscription)
  /** Lens to NavigationState */
  val navigation: Lens[ApplicationState, NavigationState] = GenLens[ApplicationState](_.navigation)
  /** Lens to BettingState */
  val betting: Lens[ApplicationState, BettingState] = GenLens[ApplicationState](_.betting)
  /** Lens to WebServiceState */
  val webService: Lens[ApplicationState, WebServiceState] = GenLens[ApplicationState](_.webService)

  /** Initial state */
  def empty = new ApplicationState(
    MarketSubscriptionState.empty,
    NavigationState.empty,
    BettingState.empty,
    WebServiceState.empty
  )
}
