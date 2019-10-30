package com.org.fplab.liveinfostream.betfair.betting.models

import io.circe.Json

trait HasEncoded {
  def encoded: Json
}
