package com.org.fplab.liveinfostream.metrics.collectors

import cats.Monad
import cats.data.NonEmptyList
import cats.effect.{Resource, Sync}
import cats.implicits._
import com.org.fplab.liveinfostream.metrics.{CollectorLabels, CollectorRegistry, Gauge}
import com.org.fplab.liveinfostream.webservice.models.GuiMarket

trait RunnerPrice[F[_]] extends MetricsCollector[F]

object RunnerPrice {

  def make[F[_]: Sync](registry: CollectorRegistry[F]): Resource[F, RunnerPrice[F]] =
    Gauge
      .make[F, Labels](
        registry,
        name = "runner_price",
        help = "Runner price",
        labelNames = Labels("market_id", "market_name", "runner_id", "runner_name", "inplay")
      )
      .map(apply(_))

  def apply[F[_]: Monad](gauge: Gauge[F, Labels]): RunnerPrice[F] =
    new RunnerPrice[F] {

      def process(market: GuiMarket): F[Unit] =
        market.runners.traverse_ { runner =>
          for {
            child <- gauge.child(
                       Labels(
                         marketId = market.id,
                         marketName = market.name,
                         runnerId = runner.id.toString,
                         runnerName = runner.name,
                         inPlay = booleanResult(market.inPlay)
                       )
                     )
            _     <- child.set(runner.price)
          } yield ()
        }

      def remove(marketId: String): F[Unit] =
        gauge.removeMatching(_.marketId == marketId)
    }

  final case class Labels(
    marketId: String,
    marketName: String,
    runnerId: String,
    runnerName: String,
    inPlay: String
  )

  implicit val labels: CollectorLabels[Labels] =
    CollectorLabels.instance(labels =>
      NonEmptyList.of(labels.marketId, labels.marketName, labels.runnerId, labels.runnerName, labels.inPlay)
    )
}
