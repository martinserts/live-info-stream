package com.org.fplab.liveinfostream.webservice.models

/** Runner (team) details sent to external client */
final case class GuiRunner(
  id: Long,       // Runner id
  hc: Double,     // Handicap
  name: String,   // Runner name
  price: Double,  // Best price available to bet
  status: String, // Runner status
  volume: Double  // Total matched volume
)
