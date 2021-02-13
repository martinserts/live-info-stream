package com.org.fplab.liveinfostream.metrics.collectors

import com.org.fplab.liveinfostream.webservice.models.GuiMarket

trait MetricsCollector[F[_]] {
  def process(market: GuiMarket): F[Unit]
  def remove(marketId: String): F[Unit]

  protected def booleanResult(result: Boolean): String =
    if (result) "yes" else "no"
}
