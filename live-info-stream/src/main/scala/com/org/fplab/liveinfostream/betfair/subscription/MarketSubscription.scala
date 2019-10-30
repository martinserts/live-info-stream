package com.org.fplab.liveinfostream.betfair.subscription

import cats.data._
import cats.effect._
import cats.effect.std.Queue
import cats.implicits._
import com.betfair.esa.client.cache.market.MarketSnap
import com.org.fplab.liveinfostream.betfair.subscription.limiter.RateLimiter
import com.org.fplab.liveinfostream.betfair.subscription.limiter.RateLimiter.RateLimiterRegistry
import com.org.fplab.liveinfostream.betfair.subscription.models.LocalMarket
import com.org.fplab.liveinfostream.betfair.subscription.state.MarketSubscriptionState
import com.org.fplab.liveinfostream.metrics.Prometheus
import com.org.fplab.liveinfostream.state.ApplicationState
import com.org.fplab.liveinfostream.webservice.core.{MarketChangeExtractor, MarketConverter}
import com.org.fplab.liveinfostream.webservice.models.{ApiCommand, MarketStatusChangedCommand}
import fs2._
import fs2.concurrent._

object MarketSubscription {

  /** This is called from Betfair Java api
    * Put the message into queue, which will be later read by another process
    * Store metrics in Prometheus
    */
  def onExternalMessage[F[_]: Sync](
    market: MarketSnap,
    queue: Queue[F, LocalMarket],
    applicationState: Ref[F, ApplicationState[F]],
    prometheus: Prometheus[F]
  ): F[Unit] = {
    val localMarket = LocalMarket.fromMarketSnap(market)
    for {
      _        <- Stream.emit(localMarket).enqueueUnterminated(queue).compile.drain
      state    <- applicationState.get
      guiMarket = MarketConverter.toGuiMarket(
                    marketNameResolver = state.navigation.markets.get,
                    eventNameResolver = state.navigation.events.get,
                    runnerNameResolver = state.betting.runners.get
                  )(localMarket)
      _        <- guiMarket.traverse_(prometheus.processMessage)
    } yield ()
  }

  /** Starts queue reader */
  def createMarketChangeQueueProcessorStream[F[_]: Async](
    interrupter: SignallingRef[F, Boolean],
    stateRef: Ref[F, ApplicationState[F]],
    queue: Queue[F, LocalMarket],
    topic: Topic[F, Option[String]]
  ): Stream[F, Unit] =
    for {
      applicationState <- Stream.eval(stateRef.get)
      rateLimitersLens  = ApplicationState.marketSubscription[F] composeLens MarketSubscriptionState.rateLimiters[F]
      rateLimiters      = rateLimitersLens.get(applicationState)
      stream           <- Stream
                            .fromQueueUnterminated(queue)
                            .evalMap(localMarket => stateRef.modifyState(updateState(localMarket)))
                            .unNone                 // Get rid of empty changes
                            .flatMap(commands => Stream.emits(commands))
                            .evalMap(processCommand[F](rateLimiters, _))
                            .unNone                 // Filter out commands, which where rejected by rate limiter
                            .map(command => Some(command.getJson.noSpaces))
                            .through(topic.publish) // Send changes to topic
                            .interruptWhen(interrupter)
    } yield stream

  /** Updates market subscription state
    *
    * Initially finds old corresponding market (by way of marketId)
    * Determines changes between old and incoming market and returns them.
    * Saves updatedMarket in market subscription state
    */
  private def updateState[F[_]](updatedMarket: LocalMarket): State[ApplicationState[F], Option[List[ApiCommand]]] =
    for {
      applicationState       <- State.get[ApplicationState[F]]
      marketsLens             = ApplicationState.marketSubscription[F] composeLens MarketSubscriptionState.markets[F]
      markets                 = marketsLens.get(applicationState)
      oldLocalMarket          = markets.get(updatedMarket.marketId)
      changesAsApiCommandList = oldLocalMarket.flatMap(MarketChangeExtractor.getStateChanges(_, updatedMarket))
      updatedMarkets          = if (isAnyMarketObsolete(changesAsApiCommandList)) markets.removed(updatedMarket.marketId)
                                else markets.updated(updatedMarket.marketId, updatedMarket)
      newApplicationState     = marketsLens.set(updatedMarkets)(applicationState)
      _                      <- State.set[ApplicationState[F]](newApplicationState)
    } yield changesAsApiCommandList

  /** We should delete closed markets from our state */
  private def isAnyMarketObsolete(commands: Option[List[ApiCommand]]): Boolean =
    commands.exists(_.exists(command => isMarketObsolete(command).isDefined))

  /** Returns marketId if obsolete */
  private def isMarketObsolete(command: ApiCommand): Option[String] =
    command match {
      case MarketStatusChangedCommand(marketId, "CLOSED", _) => Option(marketId)
      case _                                                 => None
    }

  private def removeRateLimiters[F[_]: Sync](registryRef: Ref[F, RateLimiterRegistry[F]], marketId: String): F[Unit] =
    for {
      registry   <- registryRef.get
      toBeRemoved = registry.filter {
                      case (_, RateLimiter(_, Some(id), _)) => id == marketId
                      case _                                => false
                    }
      _          <- toBeRemoved.values.toList.traverse(_.stop) // Stop corresponding rate limiters
      _          <- registryRef.set(registry -- toBeRemoved.keys) // Remove rate limiters from registry
    } yield ()

  private def processCommand[F[_]: Async](
    registryRef: Ref[F, RateLimiterRegistry[F]],
    command: ApiCommand
  ): F[Option[ApiCommand]] =
    isMarketObsolete(command) match {
      case Some(marketId) => removeRateLimiters(registryRef, marketId).as(Some(command))
      case None           => limitRate(registryRef, command) // TODO: OptionT.semiFlatmap
    }

  private def limitRate[F[_]: Async](
    registryRef: Ref[F, RateLimiterRegistry[F]],
    command: ApiCommand
  ): F[Option[ApiCommand]] =
    for {
      rateLimiter <- getOrCreateRateLimiter(registryRef, command)
      success     <- rateLimiter.traverse { rl =>
                       rl.limiter
                         .submit(Sync[F].unit)
                         .as(true)
                         .handleError(_ => false)
                     }
    } yield if (success.getOrElse(true)) Some(command) else None

  private def getOrCreateRateLimiter[F[_]: Async](
    registryRef: Ref[F, RateLimiterRegistry[F]],
    command: ApiCommand
  ): F[Option[RateLimiter[F]]] =
    command.getRateLimiter.traverse(limitDefinition =>
      for {
        registry    <- registryRef.get
        rateLimiter <- OptionT
                         .fromOption[F](registry.get(limitDefinition.key))
                         .getOrElseF(
                           RateLimiter.launch(
                             limitDefinition, // Rate limiter not found - launch new one
                             command.getAssociatedMarketId,
                             registryRef
                           )
                         )
      } yield rateLimiter
    )
}
