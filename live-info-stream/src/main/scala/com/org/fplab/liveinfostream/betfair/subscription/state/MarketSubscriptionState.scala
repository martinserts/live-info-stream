package com.org.fplab.liveinfostream.betfair.subscription.state

import cats.implicits._
import cats.effect.Sync
import cats.effect.concurrent.Ref
import com.org.fplab.liveinfostream.betfair.subscription.limiter.RateLimiter.RateLimiterRegistry
import com.org.fplab.liveinfostream.betfair.subscription.models.LocalMarket
import monocle._
import monocle.macros.GenLens

import scala.collection.immutable.Map


/** Subscription state contains map of market id -> market details */
case class MarketSubscriptionState[F[_]](
                                    markets: Map[String, LocalMarket], // MarketId -> LocalMarket
                                    rateLimiters: Ref[F, RateLimiterRegistry[F]]
                                  )

object MarketSubscriptionState {
  // Lenses
  def markets[F[_]]: Lens[MarketSubscriptionState[F], Map[String, LocalMarket]] = GenLens[MarketSubscriptionState[F]](_.markets)
  def rateLimiters[F[_]]: Lens[MarketSubscriptionState[F], Ref[F, RateLimiterRegistry[F]]] = GenLens[MarketSubscriptionState[F]](_.rateLimiters)

  def empty[F[_] : Sync]: F[MarketSubscriptionState[F]] = for {
    rateLimiters <- Ref.of[F, RateLimiterRegistry[F]](Map.empty)
  } yield MarketSubscriptionState(Map.empty, rateLimiters)
}