package com.org.fplab.liveinfostream.metrics

import cats.effect.Sync
import cats.implicits._
import io.prometheus.client.{Collector => JCollector, CollectorRegistry => JCollectorRegistry}

import java.io.StringWriter
import io.prometheus.client.exporter.common.TextFormat

trait CollectorRegistry[F[_]] {
  def register(collector: JCollector): F[Unit]
  def unregister(collector: JCollector): F[Unit]
  def clear(): F[Unit]
  def metrics: F[String]
}

object CollectorRegistry {

  def of[F[_]: Sync]: F[CollectorRegistry[F]] =
    Sync[F]
      .delay(new JCollectorRegistry())
      .map(apply(_))

  def apply[F[_]: Sync](registry: JCollectorRegistry): CollectorRegistry[F] =
    new CollectorRegistry[F] {

      def register(collector: JCollector): F[Unit] =
        Sync[F].delay { registry.register(collector) }

      def unregister(collector: JCollector): F[Unit] =
        Sync[F].delay { registry.unregister(collector) }

      def clear(): F[Unit] =
        Sync[F].delay { registry.clear() }

      def metrics: F[String] =
        Sync[F].delay {
          val writer = new StringWriter
          TextFormat.write004(writer, registry.metricFamilySamples)
          writer.toString
        }
    }
}
