package com.org.fplab.liveinfostream.betfair.subscription.limiter

import cats.implicits._
import cats.effect.{Temporal, Concurrent, Deferred, Ref}
import com.org.fplab.liveinfostream.webservice.models.ApiCommandLimitRate
import fs2.concurrent.SignallingRef
import upperbound.Limiter

final case class RateLimiter[F[_]](
  limiter: Limiter[F],
  marketId: Option[String],
  interrupter: SignallingRef[F, Boolean]
) {
  def stop: F[Unit] = interrupter.set(true)
}

object RateLimiter {
  type RateLimiterRegistry[F[_]] = Map[String, RateLimiter[F]]

  def launch[F[_]: Temporal](
    rate: ApiCommandLimitRate,
    marketId: Option[String],
    registryRef: Ref[F, RateLimiterRegistry[F]],
    maxQueuedItems: Int = 1
  ): F[RateLimiter[F]] =
    for {
      // Signals that limiters needs to be stopped
      interrupter      <- SignallingRef[F, Boolean](false)
      // Signals when limiter is added to registry
      registryModified <- Deferred[F, RateLimiter[F]]

      _           <- Concurrent[F].start(Limiter.start(rate.rate, maxQueuedItems).use { limiter =>
                       val rl = RateLimiter(limiter, marketId, interrupter)
                       // Add RateLimiter to registry
                       registryRef.modify(registry => (registry.updated(rate.key, rl), ())) *>
                         registryModified.complete(rl) *>
                         // Wait for completion
                         interrupter.discrete.compile.drain *>
                         // Remove RateLimiter to registry when finished
                         registryRef.modify(registry => (registry.removed(rate.key), ()))
                     })

      // Get limiter as soon as it is added to the registry
      rateLimiter <- registryModified.get
    } yield rateLimiter
}
