package com.org.fplab.liveinfostream.metrics

import cats.Monad
import cats.data.OptionT
import cats.effect.{Ref, Sync}
import cats.implicits._

trait ChildCollection[F[_], K, V] {
  def get(labels: K): F[V]
  def removeMatching(p: K => Boolean): F[Unit]
}

object ChildCollection {

  def of[F[_]: Sync, K, V](
    build: List[String] => F[V],
    remove: List[String] => F[Unit]
  )(implicit
    label: CollectorLabels[K]
  ): F[ChildCollection[F, K, V]] =
    Ref
      .of[F, Map[K, V]](Map.empty[K, V])
      .map(apply(_, build, remove))

  def apply[F[_]: Monad, K, V](
    state: Ref[F, Map[K, V]],
    build: List[String] => F[V],
    remove: List[String] => F[Unit]
  )(implicit
    label: CollectorLabels[K]
  ): ChildCollection[F, K, V] =
    new ChildCollection[F, K, V] {

      def get(labels: K): F[V] =
        OptionT(state.get.map(_.get(labels)))
          .getOrElseF(
            for {
              entity <- build(label.toNel(labels).toList)
              _      <- state.update(_.updated(labels, entity))
            } yield entity
          )

      def removeMatching(p: K => Boolean): F[Unit] =
        for {
          labels <- state.get.map(_.keys.filter(p))
          _      <- labels.toList
                      .map(label.toNel(_).toList)
                      .traverse_(remove)
          _      <- state.update(_ -- labels.toSet)
        } yield ()
    }
}
