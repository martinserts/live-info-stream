package com.org.fplab.liveinfostream.betfair.navigation.models

import com.org.fplab.liveinfostream.betfair.navigation.models.NavigationGroupChildCodec._
import io.circe.Decoder

/** Children of type "NavigationGroup" */
case class NavigationGroup(id: String,
                           name: String,
                           children: Option[List[NavigationGroupChild]],
                          `type`: String
                          ) {
  def isEvent = `type` == "EVENT"
  def isGroup = `type` == "GROUP"
}

object NavigationGroupCodec {
  lazy implicit val decodeNavigationGroup: Decoder[NavigationGroup] =
    Decoder.forProduct4(
      "id",
      "name",
      "children",
      "type"
    )(NavigationGroup.apply)
      .ensure(ng => ng.isGroup || ng.isEvent, "Type must be GROUP or EVENT")
}
