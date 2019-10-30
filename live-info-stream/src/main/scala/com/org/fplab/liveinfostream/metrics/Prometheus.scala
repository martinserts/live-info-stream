package com.org.fplab.liveinfostream.metrics

import cats.Monad
import cats.effect.{Clock, Resource, Sync}
import cats.implicits._
import com.org.fplab.liveinfostream.metrics.collectors.{
  MarketTradedVolume,
  MetricsCollector,
  RunnerPrice,
  RunnerTradedVolume
}
import com.org.fplab.liveinfostream.webservice.models.GuiMarket

import scala.concurrent.duration._

trait Prometheus[F[_]] {
  def processMessage(market: GuiMarket): F[Unit]
  def metrics: F[String]
}

object Prometheus {

  private val ProcessBeforeEvent = 20.minutes
  private val StopAfterEvent     = 60.minutes

  def make[F[_]: Sync]: Resource[F, Prometheus[F]] =
    for {
      registry           <- Resource.eval { CollectorRegistry.of[F] }
      marketTradedVolume <- MarketTradedVolume.make(registry)
      runnerPrice        <- RunnerPrice.make(registry)
      runnerTradedVolume <- RunnerTradedVolume.make(registry)
    } yield apply(registry, marketTradedVolume, runnerPrice, runnerTradedVolume)

  def apply[F[_]: Monad: Clock](
    registry: CollectorRegistry[F],
    marketTradedVolume: MarketTradedVolume[F],
    runnerPrice: RunnerPrice[F],
    runnerTradedVolume: RunnerTradedVolume[F]
  ): Prometheus[F] =
    new Prometheus[F] {

      def processMessage(market: GuiMarket): F[Unit] =
        Clock[F].realTimeInstant.flatMap { now =>
          val nearOrStarted = market.marketTime
            .minusMillis(ProcessBeforeEvent.toMillis)
            .isBefore(now)
          Monad[F].whenA(nearOrStarted) {
            val metrics: List[MetricsCollector[F]]     = List(marketTradedVolume, runnerPrice, runnerTradedVolume)
            val closed                                 = market.status == "CLOSED"
            val stale                                  = market.marketTime
              .plusMillis(StopAfterEvent.toMillis)
              .isBefore(now)
            val action: MetricsCollector[F] => F[Unit] =
              if (closed || stale) _.remove(market.id) else _.process(market)
            metrics.traverse_(action)
          }
        }

      def metrics: F[String] = registry.metrics
    }
}
