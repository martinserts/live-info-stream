package com.org.fplab.liveinfostream.betfair.navigation.models

import com.org.fplab.liveinfostream.betfair.navigation.models.NavigationGroupCodec._
import com.org.fplab.liveinfostream.betfair.navigation.models.NavigationMarketCodec._
import io.circe.{Decoder, HCursor}

sealed trait NavigationGroupChild

final case class NgcGroup(data: NavigationGroup)   extends NavigationGroupChild
final case class NgcMarket(data: NavigationMarket) extends NavigationGroupChild

object NavigationGroupChildCodec {
  implicit lazy val decodeNgcGroup: Decoder[NgcGroup]   = Decoder[NavigationGroup].map(NgcGroup(_))
  implicit lazy val decodeNgcMarket: Decoder[NgcMarket] = Decoder[NavigationMarket].map(NgcMarket(_))

  implicit lazy val decodeNavigationGroupChild: Decoder[NavigationGroupChild] =
    (c: HCursor) => {
      for {
        t   <- c.downField("type").as[String]
        ngc <- t match {
                 case "MARKET"                               => Decoder[NgcMarket].apply(c)
                 case t if List("GROUP", "EVENT") contains t => Decoder[NgcGroup].apply(c)
               }
      } yield ngc
    }
}
