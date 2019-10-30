package com.org.fplab.liveinfostream.metrics

import cats.data.NonEmptyList

trait CollectorLabels[A] {
  def toNel(a: A): NonEmptyList[String]
}

object CollectorLabels {

  def apply[A](implicit instance: CollectorLabels[A]): CollectorLabels[A] = instance

  def instance[A](f: A => NonEmptyList[String]): CollectorLabels[A] = (a: A) => f(a)
}
