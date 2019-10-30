package com.org.fplab.liveinfostream.webservice.models

import java.time.Instant

/** Market details sent to external client */
case class GuiMarket(
                      id: String,                   // Market id
                      name: String,                 // Market name
                      inPlay: Boolean,              // Whether market is currently in play (live)
                      marketTime: Instant,          // Instant when market goes live
                      status: String,               // Market status (OPEN, SUSPENDED, CLOSED, INACTIVE)
                      event: GuiEvent,              // The event this market belongs to
                      runners: List[GuiRunner],     // Runners (teams) of the market
                      tradedVolume: Double          // Volume traded
                    )
