package com.org.fplab.liveinfostream.betfair.subscription

import cats.data._
import cats.effect._
import cats.effect.concurrent._
import fs2._
import fs2.concurrent._
import com.betfair.esa.client.cache.market.MarketSnap
import com.org.fplab.liveinfostream.betfair.subscription.models.LocalMarket
import com.org.fplab.liveinfostream.betfair.subscription.state.MarketSubscriptionState
import com.org.fplab.liveinfostream.state.ApplicationState
import com.org.fplab.liveinfostream.webservice.core.MarketChangeExtractor
import com.org.fplab.liveinfostream.webservice.models.{ApiCommand, MarketStatusChangedCommand}

object MarketSubscription {
  /** This is called from Betfair Java api
   * Put the message into queue, which will be later read by another process
   */
  def onExternalMessage[F[_] : Sync](market: MarketSnap,
                                     queue: Queue[F, LocalMarket]): F[Unit] = {
    val localMarket = LocalMarket.fromMarketSnap(market)
    Stream.emit(localMarket).through(queue.enqueue).compile.drain
  }

  /** Starts queue reader */
  def createMarketChangeQueueProcessorStream[F[_] : Sync : Concurrent](interrupter: SignallingRef[F, Boolean],
                                                                       stateRef: Ref[F, ApplicationState],
                                                                       queue: Queue[F, LocalMarket],
                                                                       topic: Topic[F, Option[String]]
                                                                      ): Stream[F, Unit] = {
    queue.dequeue.evalMap(processMarketChange[F](stateRef, _))
      .unNone // Get rid of empty changes
      .flatMap(commands => Stream.emits(commands.map(c => Some(c.getJson.noSpaces))))
      .through(topic.publish) // Send changes to topic
      .interruptWhen(interrupter)
  }

  /** Processes market change message */
  private def processMarketChange[F[_] : Sync](stateRef: Ref[F, ApplicationState],
                                               localMarket: LocalMarket): F[Option[List[ApiCommand]]] =
    stateRef.modifyState(updateState(localMarket))

  /** Updates market subscription state
   *
   * Initially finds old corresponding market (by way of marketId)
   * Determines changes between old and incoming market and returns them.
   * Saves updatedMarket in market subscription state
   * */
  private def updateState(updatedMarket: LocalMarket): State[ApplicationState, Option[List[ApiCommand]]] = for {
    applicationState <- State.get[ApplicationState]

    marketsLens = ApplicationState.marketSubscription composeLens MarketSubscriptionState.markets
    markets = marketsLens.get(applicationState)

    oldLocalMarket = markets.get(updatedMarket.marketId)
    changesAsApiCommandList = oldLocalMarket.flatMap(MarketChangeExtractor.getStateChanges(_, updatedMarket))

    updatedMarkets = if (isMarketObsolete(changesAsApiCommandList)) markets.removed(updatedMarket.marketId)
      else markets.updated(updatedMarket.marketId, updatedMarket)

    newApplicationState = marketsLens.set(updatedMarkets)(applicationState)
    _ <- State.set[ApplicationState](newApplicationState)
  } yield changesAsApiCommandList

  /** We should delete closed markets from our state */
  private def isMarketObsolete(commands: Option[List[ApiCommand]]): Boolean =
    commands.exists(_.exists {
      case MarketStatusChangedCommand(_, status, _) => status == "CLOSED"
      case _ => false
    })
}
