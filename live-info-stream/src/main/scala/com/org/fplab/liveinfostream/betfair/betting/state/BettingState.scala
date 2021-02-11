package com.org.fplab.liveinfostream.betfair.betting.state

import monocle.Lens
import monocle.macros.GenLens

/** Betting state that contains map of runner id -> runner name */
final case class BettingState(
  runners: Map[Long, String]
)

object BettingState {
  // Lenses
  val runners: Lens[BettingState, Map[Long, String]] = GenLens[BettingState](_.runners)

  def empty: BettingState = BettingState(Map.empty)
}
