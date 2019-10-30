package com.org.fplab.liveinfostream.betfair.navigation.models

import cats.implicits._
import cats.effect._
import com.org.fplab.liveinfostream.ConfigurationAsk
import com.org.fplab.liveinfostream.betfair.navigation.models.NavigationEventTypeCodec._
import io.circe.Decoder
import org.http4s._
import org.http4s.circe.CirceEntityDecoder._
import org.http4s.client.blaze._

import scala.concurrent.ExecutionContext

/** Navigation root. Children only of type "NavigationEventType" */
case class NavigationRoot(id: Int,
                          name: String,
                          children: List[NavigationEventType],
                         `type`: String
                         )

object NavigationRootCodec {
  lazy implicit val decodeNavigationRoot: Decoder[NavigationRoot] =
    Decoder.forProduct4(
      "id",
      "name",
      "children",
      "type"
    )(NavigationRoot.apply)
      .ensure(_.`type` == "GROUP", "Type must be GROUP")
}

object NavigationRoot {
  import NavigationRootCodec._

  /** Fetches Betfair navigation info */
  def fromUri[F[_]: ConcurrentEffect](sessionId: String)
                                     (implicit C: ConfigurationAsk[F]): F[NavigationRoot] = {
      BlazeClientBuilder[F](ExecutionContext.global).resource.use { client => for {
        config <- C.reader(_.betfair)
        headers = Headers.of(
          Header("X-Application", config.appKey.value),
          Header("X-Authentication", sessionId)
        )
        request = Request[F](Method.GET, Uri.unsafeFromString(config.navigationUri), headers = headers)
        result <- client.expect[NavigationRoot](request)
      } yield result
    }
  }
}
