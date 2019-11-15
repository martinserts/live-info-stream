package com.org.fplab.liveinfostream.webservice

import java.time.Instant

import cats._
import cats.implicits._
import cats.effect._
import cats.effect.concurrent._
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
import org.http4s.server.websocket.WebSocketBuilder
import org.http4s.websocket._

class ApiEndpoints[F[_]: Sync : Concurrent](stateRef: Ref[F, ApplicationState[F]], topic: Topic[F, Option[String]])
                                           (implicit timer: Timer[F]) extends Http4sDsl[F] {
  def eventsEndpoint: HttpRoutes[F] =
    HttpRoutes.of[F] {
      /** Market list */
      case GET -> Root / "markets" / "list" => for {
        state <- stateRef.get
        markets = MarketListController.getMarketList(state)
        resp <- Ok(markets.asJson)
      } yield resp

      /** Web socket */
      case GET -> Root / "ws" => {
        // Ignore input form client
        val fromClient: Pipe[F, WebSocketFrame, Unit] = _.void

        // Notify clients that a new user has connected
        val onlineUsers = new OnlineUserCountController(stateRef, topic)
        val addUser = Stream.eval(
          onlineUsers.addUser.map(_.getJson.noSpaces)
        )

        // Send current server time to synchronize the client
        val sendServerTime = Stream.eval(
          Sync[F].delay(Instant.now()).map(ServerTimeCommand(_).getJson.noSpaces)
        )
        val topicStream = topic.subscribe(1000).unNone

        val toClient: Stream[F, WebSocketFrame.Text] =
          (sendServerTime ++ addUser ++ topicStream).map(WebSocketFrame.Text(_))

        WebSocketBuilder[F].build(toClient, fromClient, onClose = onlineUsers.removeUser.void)
      }
    }
}
