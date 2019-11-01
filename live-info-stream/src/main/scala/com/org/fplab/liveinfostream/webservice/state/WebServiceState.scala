package com.org.fplab.liveinfostream.webservice.state

import com.org.fplab.liveinfostream.betfair.betting.state.BettingState
import monocle.Lens
import monocle.macros.GenLens

case class WebServiceState(
                            onlineUserCount: Int
                          )

object WebServiceState {
  // Lenses
  val onlineUserCount: Lens[WebServiceState, Int] = GenLens[WebServiceState](_.onlineUserCount)

  def empty = new WebServiceState(0)
}
