package com.org.fplab.liveinfostream.betfair.betting

/** Uses Betfair betting API to fetch runner (selection) information */

import cats.effect._
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
import okhttp3.OkHttpClient
import sttp.client3._
import sttp.client3.circe._
import sttp.client3.okhttp.OkHttpSyncBackend

import scala.concurrent.duration._

class BettingClient[F[_]: Async](sessionId: String)(implicit C: ConfigurationAsk[F]) {
  val HorseRacing = "7"

  /** Fetches map of runner id -> runner name */
  def fetchRunners: F[Map[Long, String]] = {
    val okHttpClient: OkHttpClient = new OkHttpClient.Builder().build()
    val backend                    = OkHttpSyncBackend.usingClient(okHttpClient)
    for {
      config       <- C.reader(_.betfair)
      request       = basicRequest
                        .header("X-Application", config.appKey.value)
                        .header("X-Authentication", sessionId)
                        .post(config.bettingUri)
                        .body(listMarketCatalogueRequest)
      response     <- Async[F].delay(request.send(backend))
      jsonResponse <- Async[F].fromEither(response.body.leftMap(new Exception(_)))
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
  private val logger = org.log4s.getLogger

  final case class RunnerData(selectionId: Long, runnerName: String)

  /** Creates a stream, that hourly reads runner info and saves it into application state */
  def createBettingAutoUpdateStream[F[_]: Async: ConfigurationAsk](
    interrupter: SignallingRef[F, Boolean],
    stateRef: Ref[F, ApplicationState[F]],
    sessionIdReader: => F[String]
  ): Stream[F, Unit] = {
    val runnersLens   = ApplicationState.betting[F] composeLens BettingState.runners
    val bettingUpdate = for {
      sessionId <- sessionIdReader
      result    <- new BettingClient(sessionId).fetchRunners.attempt
      _         <- result match {
                     case Right(runners) => stateRef.update { s => runnersLens.set(runners)(s) }
                     case Left(error)    => Async[F].delay { logger.error(error)("Failed to fetch runners") }
                   }
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
