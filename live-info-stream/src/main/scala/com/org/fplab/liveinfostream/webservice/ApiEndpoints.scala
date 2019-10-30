package com.org.fplab.liveinfostream.webservice

import java.time.Instant

import cats.implicits._
import cats.effect._
import cats.effect.concurrent._
import com.org.fplab.liveinfostream.state.ApplicationState
import com.org.fplab.liveinfostream.webservice.controllers.MarketListController
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

class ApiEndpoints[F[_]: Sync : Concurrent](stateRef: Ref[F, ApplicationState], topic: Topic[F, Option[String]])
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

        // Send current server time to synchronize the client
        val initialCommand = Stream.eval(
          Sync[F].delay(Instant.now()).map(ServerTimeCommand(_).getJson.noSpaces)
        )
        val topicStream = topic.subscribe(1000).unNone

        val toClient: Stream[F, WebSocketFrame.Text] =
          (initialCommand ++ topicStream).map(cmd => WebSocketFrame.Text(cmd))

        WebSocketBuilder[F].build(toClient, fromClient)
      }
    }
}
