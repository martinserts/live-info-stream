package com.org.fplab.liveinfostream.betfair.navigation.models

import cats.syntax.functor._
import com.org.fplab.liveinfostream.betfair.navigation.models.NavigationGroupCodec._
import com.org.fplab.liveinfostream.betfair.navigation.models.NavigationRaceCodec._
import io.circe.Decoder

sealed trait NavigationEventTypeChild

case class NetcGroup(data: NavigationGroup) extends NavigationEventTypeChild
case class NetcRace(data: NavigationRace) extends NavigationEventTypeChild

object NavigationEventTypeChildCodec {
  lazy implicit val decodeNetcGroup: Decoder[NetcGroup] = Decoder[NavigationGroup].map(NetcGroup(_))
  lazy implicit val decodeNetcRace: Decoder[NetcRace] = Decoder[NavigationRace].map(NetcRace(_))

  lazy implicit val decodeNavigationEventTypeChild: Decoder[NavigationEventTypeChild] =
    List[Decoder[NavigationEventTypeChild]](
      Decoder[NetcGroup].widen,
      Decoder[NetcRace].widen
    ).reduceLeft(_ or _)
}
