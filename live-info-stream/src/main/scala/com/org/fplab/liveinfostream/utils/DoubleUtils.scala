package com.org.fplab.liveinfostream.utils

class RichDouble(d: Double) {

  def ~==(other: Double): Boolean = same(other)

  def ~!=(other: Double): Boolean = !same(other)

  def same(other: Double): Boolean = {
    val precision: Double = 0.0001
    (d - other).abs < precision
  }
}

object DoubleUtils {

  implicit def doubleToSyntax(d: Double): RichDouble = new RichDouble(d)
}
