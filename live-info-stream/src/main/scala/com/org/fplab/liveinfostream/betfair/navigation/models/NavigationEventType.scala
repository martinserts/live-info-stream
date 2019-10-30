package com.org.fplab.liveinfostream.betfair.navigation.models

import com.org.fplab.liveinfostream.betfair.navigation.models.NavigationEventTypeChildCodec._
import io.circe.Decoder

/** Children of types "NavigationGroup" and "NavigationRace" */
case class NavigationEventType(id: String,
                               name: String,
                               children: Option[List[NavigationEventTypeChild]],
                              `type`: String
                              )

object NavigationEventTypeCodec {
  lazy implicit val decodeNavigationEventType: Decoder[NavigationEventType] =
    Decoder.forProduct4(
      "id",
      "name",
      "children",
      "type"
    )(NavigationEventType.apply)
      .ensure(_.`type` == "EVENT_TYPE", "Type must be EVENT_TYPE")
}
