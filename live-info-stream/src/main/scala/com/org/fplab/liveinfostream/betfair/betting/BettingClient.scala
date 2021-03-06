package com.org.fplab.liveinfostream.betfair.betting

/** Uses Betfair betting API to fetch runner (selection) information */

import cats.effect._
import cats.effect.concurrent.Ref
import cats.implicits._
import com.org.fplab.liveinfostream.ConfigurationAsk
import com.org.fplab.liveinfostream.betfair.betting.models.{ListMarketCatalogueRequest, MarketFilter, RpcRequest}
import com.org.fplab.liveinfostream.betfair.betting.state.BettingState
import com.org.fplab.liveinfostream.state.ApplicationState
import fs2.Stream
import fs2.concurrent.SignallingRef
import io.circe.Json
import io.circe.generic.auto._
import io.circe.optics.JsonPath._
import io.circe.parser.parse
import org.http4s._
import org.http4s.circe._
import org.http4s.client.blaze._

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

class BettingClient[F[_]: ConcurrentEffect](sessionId: String)(implicit C: ConfigurationAsk[F]) {
  val HorseRacing = "7"

  /** Fetches map of runner id -> runner name */
  def fetchRunners: F[Map[Long, String]] =
    BlazeClientBuilder[F](ExecutionContext.global).resource.use { client =>
      for {
        config       <- C.reader(_.betfair)
        headers       = Headers.of(
                          Header("X-Application", config.appKey.value),
                          Header("X-Authentication", sessionId)
                        )
        request       = Request[F](Method.POST, Uri.unsafeFromString(config.bettingUri), headers = headers)
                          .withEntity(listMarketCatalogueRequest)
        jsonResponse <- client.expect[String](request)
      } yield Map.from(BettingClient.parseRunners(jsonResponse).map(r => r.selectionId -> r.runnerName))
    }

  /** list market catalogue API request as JSON */
  private def listMarketCatalogueRequest: Json = {
    val listMarketCatalogueRequest: ListMarketCatalogueRequest = ListMarketCatalogueRequest(
      MarketFilter(eventTypeIds = List(HorseRacing), turnInPlayEnabled = true, marketBettingTypes = List("ODDS")),
      marketProjection = List("RUNNER_DESCRIPTION"),
      sort = "FIRST_TO_START",
      maxResults = 1000
    )
    RpcRequest("listMarketCatalogue", listMarketCatalogueRequest).getJson
  }
}

object BettingClient {

  final case class RunnerData(selectionId: Long, runnerName: String)

  /** Creates a stream, that hourly reads runner info and saves it into application state */
  def createBettingAutoUpdateStream[F[_]: ConcurrentEffect: Timer: ConfigurationAsk](
    interrupter: SignallingRef[F, Boolean],
    stateRef: Ref[F, ApplicationState[F]],
    sessionIdReader: => F[String]
  ): Stream[F, Unit] = {
    val runnersLens   = ApplicationState.betting[F] composeLens BettingState.runners
    val bettingUpdate = for {
      sessionId <- sessionIdReader
      runners   <- new BettingClient(sessionId).fetchRunners
      _         <- stateRef.update { s => runnersLens.set(runners)(s) }
    } yield ()

    (Stream.eval(bettingUpdate) ++ Stream.sleep(1.hour)).repeat
      .interruptWhen(interrupter)
  }

  /** Parses API response into runner data */
  def parseRunners(json: String): List[RunnerData] = {
    val jPath = root.result.each.runners.each.as[RunnerData]
    parse(json).map(jPath.getAll).getOrElse(List.empty)
  }
}
