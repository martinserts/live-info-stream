package com.org.fplab.liveinfostream.metrics

import cats.effect.{Resource, Sync}
import cats.implicits._
import io.prometheus.client.{Gauge => JGauge}

trait Gauge[F[_], A] {
  def child(labels: A): F[Gauge.Child[F]]
  def removeMatching(p: A => Boolean): F[Unit]
}

object Gauge {

  def make[F[_]: Sync, A](
    registry: CollectorRegistry[F],
    name: String,
    help: String,
    labelNames: A
  )(implicit
    label: CollectorLabels[A]
  ): Resource[F, Gauge[F, A]] = {
    val resource = for {
      gauge    <- Sync[F].delay {
                    JGauge
                      .build()
                      .name(name)
                      .help(help)
                      .labelNames(label.toNel(labelNames).toList: _*)
                      .create()
                  }
      _        <- registry.register(gauge)
      children <- ChildCollection.of[F, A, Gauge.Child[F]](
                    build = labels =>
                      Sync[F]
                        .delay { gauge.labels(labels: _*) }
                        .map(Gauge.Child[F]),
                    remove = labels =>
                      Sync[F]
                        .delay { gauge.remove(labels: _*) }
                  )
    } yield apply(children) -> registry.unregister(gauge)
    Resource(resource)
  }

  def apply[F[_], A](
    children: ChildCollection[F, A, Gauge.Child[F]]
  ): Gauge[F, A] =
    new Gauge[F, A] {
      def child(labels: A): F[Gauge.Child[F]]      = children.get(labels)
      def removeMatching(p: A => Boolean): F[Unit] = children.removeMatching(p)
    }

  trait Child[F[_]] {
    def dec(): F[Unit]
    def dec(amount: Double): F[Unit]
    def inc(): F[Unit]
    def inc(amount: Double): F[Unit]
    def set(amount: Double): F[Unit]
  }

  object Child {

    def apply[F[_]: Sync](child: JGauge.Child): Child[F] =
      new Child[F] {

        def dec(): F[Unit] =
          Sync[F].delay { child.dec() }

        def dec(amount: Double): F[Unit] =
          Sync[F].delay { child.dec(amount) }

        def inc(): F[Unit] =
          Sync[F].delay { child.inc() }

        def inc(amount: Double): F[Unit] =
          Sync[F].delay { child.inc(amount) }

        def set(amount: Double): F[Unit] =
          Sync[F].delay { child.set(amount) }
      }
  }
}
