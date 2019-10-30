package com.org.fplab.liveinfostream.betfair.subscription.state

import com.org.fplab.liveinfostream.betfair.subscription.models.LocalMarket
import monocle._
import monocle.macros.GenLens

import scala.collection.immutable.Map


/** Subscription state contains map of market id -> market details */
case class MarketSubscriptionState(
                                    markets: Map[String, LocalMarket] // MarketId -> LocalMarket
                                  )

object MarketSubscriptionState {
  // Lenses
  val markets: Lens[MarketSubscriptionState, Map[String, LocalMarket]] = GenLens[MarketSubscriptionState](_.markets)

  def empty = MarketSubscriptionState(Map.empty)
}