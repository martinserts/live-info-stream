package com.org.fplab.liveinfostream.betfair.betting.models

import io.circe._
import io.circe.generic.auto._
import io.circe.syntax._

/** Betfair betting API request */
final case class ListMarketCatalogueRequest(
  filter: MarketFilter,
  marketProjection: List[String],
  sort: String,
  maxResults: Int
) extends HasEncoded {
  def encoded: Json = this.asJson
}
