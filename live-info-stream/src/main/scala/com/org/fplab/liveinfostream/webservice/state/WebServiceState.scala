package com.org.fplab.liveinfostream.webservice.state

import monocle.Lens
import monocle.macros.GenLens

final case class WebServiceState(onlineUserCount: Int)

object WebServiceState {
  // Lenses
  val onlineUserCount: Lens[WebServiceState, Int] = GenLens[WebServiceState](_.onlineUserCount)

  def empty: WebServiceState = WebServiceState(0)
}
