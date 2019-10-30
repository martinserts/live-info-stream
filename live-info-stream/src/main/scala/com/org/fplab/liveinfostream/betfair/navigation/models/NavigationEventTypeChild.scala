package com.org.fplab.liveinfostream.betfair.navigation.models

import cats.syntax.functor._
import com.org.fplab.liveinfostream.betfair.navigation.models.NavigationGroupCodec._
import com.org.fplab.liveinfostream.betfair.navigation.models.NavigationRaceCodec._
import io.circe.Decoder

sealed trait NavigationEventTypeChild

final case class NetcGroup(data: NavigationGroup) extends NavigationEventTypeChild
final case class NetcRace(data: NavigationRace)   extends NavigationEventTypeChild

object NavigationEventTypeChildCodec {
  implicit lazy val decodeNetcGroup: Decoder[NetcGroup] = Decoder[NavigationGroup].map(NetcGroup)
  implicit lazy val decodeNetcRace: Decoder[NetcRace]   = Decoder[NavigationRace].map(NetcRace)

  implicit lazy val decodeNavigationEventTypeChild: Decoder[NavigationEventTypeChild] =
    Decoder[NetcGroup].widen or Decoder[NetcRace].widen
}
