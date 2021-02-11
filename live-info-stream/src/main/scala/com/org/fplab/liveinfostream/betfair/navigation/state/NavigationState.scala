package com.org.fplab.liveinfostream.betfair.navigation.state

import com.org.fplab.liveinfostream.betfair.navigation.models._
import monocle._

final case class NavigationState(
  root: Option[NavigationRoot],
  events: Map[String, String], // EventId, Event name
  markets: Map[String, String] // MarketId, Market name
)

object NavigationState {
  // Lenses
  val root: Optional[NavigationState, NavigationRoot] = Optional[NavigationState, NavigationRoot] { _.root } { r => _ =>
    fromNavigationRoot(r)
  }

  def empty: NavigationState = NavigationState(None, Map.empty, Map.empty)

  private def fromNavigationRoot(navigationRoot: NavigationRoot): NavigationState = {
    val entities = NavigationEntity.enumerate(navigationRoot)
    val events   = entities.collect { case NavigationEventEntity(e) => e.id -> e.name }
    val markets  = entities.collect { case NavigationMarketEntity(e) => e.id -> e.name }

    NavigationState(
      Some(navigationRoot),
      Map.from(events),
      Map.from(markets)
    )
  }
}
