package com.org.fplab.liveinfostream.state

import cats.implicits._
import cats.effect.Sync
import com.org.fplab.liveinfostream.betfair.betting.state.BettingState
import com.org.fplab.liveinfostream.betfair.navigation.state.NavigationState
import com.org.fplab.liveinfostream.betfair.subscription.state.MarketSubscriptionState
import com.org.fplab.liveinfostream.webservice.state.WebServiceState
import monocle._
import monocle.macros.GenLens

/** The root of application state */
case class ApplicationState[F[_]](
                             marketSubscription: MarketSubscriptionState[F],  // State of live stream data
                             navigation: NavigationState,                     // State of navigation data (refreshed hourly)
                             betting: BettingState,                           // State contains runner id/names
                             webService: WebServiceState                      // State contains online user count
                           )

object ApplicationState {
  /** Lens to MarketSubscriptionState */
  def marketSubscription[F[_]]: Lens[ApplicationState[F], MarketSubscriptionState[F]] = GenLens[ApplicationState[F]](_.marketSubscription)
  /** Lens to NavigationState */
  def navigation[F[_]]: Lens[ApplicationState[F], NavigationState] = GenLens[ApplicationState[F]](_.navigation)
  /** Lens to BettingState */
  def betting[F[_]]: Lens[ApplicationState[F], BettingState] = GenLens[ApplicationState[F]](_.betting)
  /** Lens to WebServiceState */
  def webService[F[_]]: Lens[ApplicationState[F], WebServiceState] = GenLens[ApplicationState[F]](_.webService)

  /** Initial state */
  def empty[F[_] : Sync]: F[ApplicationState[F]] = for {
    marketSubscriptionState <- MarketSubscriptionState.empty[F]
  } yield ApplicationState(
    marketSubscriptionState,
    NavigationState.empty,
    BettingState.empty,
    WebServiceState.empty
  )
}
