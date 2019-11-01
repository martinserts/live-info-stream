package com.org.fplab.liveinfostream.webservice.models

import java.time.Instant

import io.circe._
import io.circe.generic.auto._
import io.circe.syntax._

/** Command to be sent to clients via web socket message */
sealed trait ApiCommand {
  val command: String
  def getJson: Json
}

case class ServerTimeCommand(
                              instant: Instant,
                              command: String = "serverTime"
                            ) extends ApiCommand {
  def getJson: Json = this.asJson
}

case class TradedVolumeChangedCommand(
                                       marketId: String,
                                       tradedVolume: Double,
                                       command: String = "tradedVolumeChanged"
                                     ) extends ApiCommand {
  def getJson: Json = this.asJson
}

case class MarketInPlayChangedCommand(
                                       marketId: String,
                                       inPlay: Boolean,
                                       command: String = "inPlayChanged"
                                     ) extends ApiCommand {
  def getJson: Json = this.asJson
}

case class MarketStatusChangedCommand(
                                       marketId: String,
                                       status: String,
                                       command: String = "marketStatusChanged"
                                     ) extends ApiCommand {
  def getJson: Json = this.asJson
}

case class RunnerPriceChangedCommand(
                                      marketId: String,
                                      runnerId: Long,
                                      hc: Double,
                                      price: Double,
                                      command: String = "runnerPriceChanged"
                                    ) extends ApiCommand {
  def getJson: Json = this.asJson
}

case class RunnerStatusChangedCommand(
                                      marketId: String,
                                      runnerId: Long,
                                      hc: Double,
                                      status: String,
                                      command: String = "runnerStatusChanged"
                                    ) extends ApiCommand {
  def getJson: Json = this.asJson
}

case class RunnerVolumeChangedCommand(
                                       marketId: String,
                                       runnerId: Long,
                                       hc: Double,
                                       volume: Double,
                                       command: String = "runnerVolumeChanged"
                                     ) extends ApiCommand {
  def getJson: Json = this.asJson
}

case class OnlineUserCountChangedCommand(
                                       userCount: Int,
                                       command: String = "onlineUserCountChanged"
                                     ) extends ApiCommand {
  def getJson: Json = this.asJson
}
