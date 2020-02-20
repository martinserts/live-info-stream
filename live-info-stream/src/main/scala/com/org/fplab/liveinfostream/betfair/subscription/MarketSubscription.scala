package com.org.fplab.liveinfostream.betfair.subscription

import cats.MonadError
import cats.implicits._
import cats.data._
import cats.effect._
import cats.effect.concurrent._
import fs2._
import fs2.concurrent._
import com.betfair.esa.client.cache.market.MarketSnap
import com.org.fplab.liveinfostream.betfair.subscription.limiter.RateLimiter
import com.org.fplab.liveinfostream.betfair.subscription.limiter.RateLimiter.RateLimiterRegistry
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
  def createMarketChangeQueueProcessorStream[F[_] : Sync : Concurrent : Timer](interrupter: SignallingRef[F, Boolean],
                                                                               stateRef: Ref[F, ApplicationState[F]],
                                                                               queue: Queue[F, LocalMarket],
                                                                               topic: Topic[F, Option[String]]
                                                                              ): Stream[F, Unit] = for {
    applicationState <- Stream.eval(stateRef.get)
    rateLimitersLens = ApplicationState.marketSubscription[F] composeLens MarketSubscriptionState.rateLimiters[F]
    rateLimiters = rateLimitersLens.get(applicationState)
    stream <- queue.dequeue.evalMap(processMarketChange[F](stateRef, _))
      .unNone // Get rid of empty changes
      .flatMap(commands => Stream.emits(commands))
      .evalMap(processCommand[F](rateLimiters)(_))
      .unNone // Filter out commands, which where rejected by rate limiter
      .map(command => Some(command.getJson.noSpaces))
      .through(topic.publish) // Send changes to topic
      .interruptWhen(interrupter)
  } yield stream

  /** Processes market change message */
  private def processMarketChange[F[_] : Sync](stateRef: Ref[F, ApplicationState[F]],
                                               localMarket: LocalMarket): F[Option[List[ApiCommand]]] =
    stateRef.modifyState(updateState(localMarket))

  /** Updates market subscription state
   *
   * Initially finds old corresponding market (by way of marketId)
   * Determines changes between old and incoming market and returns them.
   * Saves updatedMarket in market subscription state
   * */
  private def updateState[F[_]](updatedMarket: LocalMarket): State[ApplicationState[F], Option[List[ApiCommand]]] = for {
    applicationState <- State.get[ApplicationState[F]]

    marketsLens = ApplicationState.marketSubscription[F] composeLens MarketSubscriptionState.markets[F]
    markets = marketsLens.get(applicationState)

    oldLocalMarket = markets.get(updatedMarket.marketId)
    changesAsApiCommandList = oldLocalMarket.flatMap(MarketChangeExtractor.getStateChanges(_, updatedMarket))

    updatedMarkets = if (isAnyMarketObsolete(changesAsApiCommandList)) markets.removed(updatedMarket.marketId)
      else markets.updated(updatedMarket.marketId, updatedMarket)

    newApplicationState = marketsLens.set(updatedMarkets)(applicationState)
    _ <- State.set[ApplicationState[F]](newApplicationState)
  } yield changesAsApiCommandList

  /** We should delete closed markets from our state */
  private def isAnyMarketObsolete(commands: Option[List[ApiCommand]]): Boolean =
    commands.exists(_.exists(isMarketObsolete(_).isDefined))

  /** Returns marketId if obsolete */
  private def isMarketObsolete(command: ApiCommand): Option[String] = command match {
    case MarketStatusChangedCommand(marketId, "CLOSED", _) => Option(marketId)
    case _ => None
  }

  private def removeRateLimiters[F[_] : Sync](registryRef: Ref[F, RateLimiterRegistry[F]], marketId: String): F[Unit] = for {
    registry <- registryRef.get
    toBeRemoved = registry.toList.filter {
      case (_, RateLimiter(_, Some(id), _)) => id == marketId
    }

    _ <- toBeRemoved.map(_._2).traverse(_.stop)     // Stop corresponding rate limiters

    updatedRegistry =  toBeRemoved.foldl(registry) {
      case (m: RateLimiterRegistry[F], (key, _)) => m.removed(key)
    }
    _ <- registryRef.set(updatedRegistry)           // Remove rate limiters from registry
  } yield ()

  private def processCommand[F[_] : Sync : Concurrent : Timer](registryRef: Ref[F, RateLimiterRegistry[F]])
                                                              (command: ApiCommand): F[Option[ApiCommand]] =
    isMarketObsolete(command) match {
      case Some(marketId) => for {
        _ <- removeRateLimiters(registryRef, marketId)
      } yield(Option(command))
      case None => limitRate(registryRef, command)
    }

  private def limitRate[F[_] : Sync : Concurrent : Timer](registryRef: Ref[F, RateLimiterRegistry[F]],
                                                         command: ApiCommand): F[Option[ApiCommand]] = for {
    rateLimiter <- getOrCreateRateLimiter(registryRef, command)

    result <- rateLimiter match {
      case Some(rl) => rl.limiter.submit(Sync[F].unit)        // Submit to rate limiter with dummy operation
        .map(_ => Option(command))                            // If successful, return ApiCommand
        .handleError(_ => None)                               // If not successful - return None
      case None => Sync[F].pure(Option(command))              // If there is no rate limiter, we send command unconditionally
    }
  } yield result

  private def getOrCreateRateLimiter[F[_] : Sync : Concurrent : Timer](registryRef: Ref[F, RateLimiterRegistry[F]],
                                                               command: ApiCommand): F[Option[RateLimiter[F]]] =
    command.getRateLimiter.traverse(limitDefinition => for {
      registry <- registryRef.get

      rateLimiter <- registry.get(limitDefinition.key) match {
        case Some(rateLimiter) => Sync[F].pure(rateLimiter)               // Rate limited already exists in registry - return it
        case None => RateLimiter.launch(limitDefinition,                  // Rate limiter not found - launch new one
          command.getAssociatedMarketId,
          registryRef)
      }
    } yield rateLimiter)
}
