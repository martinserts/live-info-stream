package com.org.fplab.liveinfostream.metrics.collectors

import cats.Monad
import cats.data.NonEmptyList
import cats.effect.{Resource, Sync}
import cats.implicits._
import com.org.fplab.liveinfostream.metrics.{CollectorLabels, CollectorRegistry, Gauge}
import com.org.fplab.liveinfostream.webservice.models.GuiMarket

trait MarketTradedVolume[F[_]] extends MetricsCollector[F]

object MarketTradedVolume {

  def make[F[_]: Sync](registry: CollectorRegistry[F]): Resource[F, MarketTradedVolume[F]] =
    Gauge
      .make[F, Labels](
        registry,
        name = "market_traded_volume",
        help = "Market traded volume",
        labelNames = Labels("market_id", "market_name", "inplay")
      )
      .map(apply(_))

  def apply[F[_]: Monad](gauge: Gauge[F, Labels]): MarketTradedVolume[F] =
    new MarketTradedVolume[F] {

      def process(market: GuiMarket): F[Unit] =
        for {
          child <- gauge.child(
                     Labels(
                       marketId = market.id,
                       marketName = market.name,
                       inPlay = booleanResult(market.inPlay)
                     )
                   )
          _     <- child.set(market.tradedVolume)
        } yield ()

      def remove(marketId: String): F[Unit] =
        gauge.removeMatching(_.marketId == marketId)
    }

  final case class Labels(
    marketId: String,
    marketName: String,
    inPlay: String
  )

  implicit val labels: CollectorLabels[Labels] =
    CollectorLabels.instance(labels => NonEmptyList.of(labels.marketId, labels.marketName, labels.inPlay))
}
