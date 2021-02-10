package com.org.fplab.liveinfostream.betfair.navigation.models

import cats.syntax.functor._
import com.org.fplab.liveinfostream.betfair.navigation.models.NavigationGroupCodec._
import com.org.fplab.liveinfostream.betfair.navigation.models.NavigationRaceCodec._
import io.circe.Decoder

sealed trait NavigationEventTypeChild

case class NetcGroup(data: NavigationGroup) extends NavigationEventTypeChild
case class NetcRace(data: NavigationRace)   extends NavigationEventTypeChild

object NavigationEventTypeChildCodec {
  implicit lazy val decodeNetcGroup: Decoder[NetcGroup] = Decoder[NavigationGroup].map(NetcGroup(_))
  implicit lazy val decodeNetcRace: Decoder[NetcRace]   = Decoder[NavigationRace].map(NetcRace(_))

  implicit lazy val decodeNavigationEventTypeChild: Decoder[NavigationEventTypeChild] =
    List[Decoder[NavigationEventTypeChild]](
      Decoder[NetcGroup].widen,
      Decoder[NetcRace].widen
    ).reduceLeft(_ or _)
}
