package com.org.fplab.liveinfostream.webservice

import cats.effect._
import cats.implicits._
import com.org.fplab.liveinfostream.state.ApplicationState
import com.org.fplab.liveinfostream.webservice.controllers.{MarketListController, OnlineUserCountController}
import com.org.fplab.liveinfostream.webservice.models.ServerTimeCommand
import io.circe.generic.auto._
import io.circe.syntax._
import fs2._
import fs2.concurrent._
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import org.http4s.server.websocket.WebSocketBuilder2
import org.http4s.websocket._

class ApiEndpoints[F[_]: Concurrent: Clock](
  wsBuilder: WebSocketBuilder2[F],
  stateRef: Ref[F, ApplicationState[F]],
  topic: Topic[F, Option[String]],
  metrics: F[String]
) extends Http4sDsl[F] {

  def eventsEndpoint: HttpRoutes[F] =
    HttpRoutes.of[F] {
      /** Market list */
      case GET -> Root / "markets" / "list" =>
        for {
          state   <- stateRef.get
          markets <- MarketListController.marketList(state)
          resp    <- Ok(markets.asJson)
        } yield resp

      /** Prometheus metrics */
      case GET -> Root / "metrics"          =>
        metrics.flatMap(Ok(_))

      /** Web socket */
      case GET -> Root / "ws"               =>
        // Notify clients that a new user has connected
        val onlineUsers = new OnlineUserCountController(stateRef, topic)
        val addUser     = Stream.eval(
          onlineUsers.addUser().map(_.getJson.noSpaces)
        )

        // Send current server time to synchronize the client
        val sendServerTime = Stream.eval(
          Clock[F].realTimeInstant.map { now =>
            ServerTimeCommand(now).getJson.noSpaces
          }
        )
        val topicStream    = topic.subscribe(1000).unNone

        wsBuilder
          .withOnClose(onlineUsers.removeUser().void)
          .build(
            send = (sendServerTime ++ addUser ++ topicStream).map(WebSocketFrame.Text(_)),
            receive = _.void // Ignore input form client
          )
    }
}
