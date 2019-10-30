package com.org.fplab.liveinfostream.betfair.navigation.models

import cats.implicits._
import cats.effect._
import com.org.fplab.liveinfostream.ConfigurationAsk
import com.org.fplab.liveinfostream.betfair.navigation.models.NavigationEventTypeCodec._
import io.circe.Decoder
import okhttp3._
import sttp.client3._
import sttp.client3.circe._
import sttp.client3.okhttp.OkHttpSyncBackend

/** Navigation root. Children only of type "NavigationEventType" */
final case class NavigationRoot(
  id: Int,
  name: String,
  children: List[NavigationEventType],
  `type`: String
)

object NavigationRootCodec {
  implicit lazy val decodeNavigationRoot: Decoder[NavigationRoot] =
    Decoder
      .forProduct4(
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
  def fromUri[F[_]: Async](sessionId: String)(implicit C: ConfigurationAsk[F]): F[NavigationRoot] = {
    val okHttpClient: OkHttpClient = new OkHttpClient.Builder().build()
    val backend                    = OkHttpSyncBackend.usingClient(okHttpClient)
    for {
      config         <- C.reader(_.betfair)
      request         = basicRequest
                          .header("X-Application", config.appKey.value)
                          .header("X-Authentication", sessionId)
                          .get(config.navigationUri)
                          .response(asJson[NavigationRoot])
      response       <- Async[F].delay(request.send(backend))
      navigationRoot <- Async[F].fromEither(response.body)
    } yield navigationRoot
  }
}
