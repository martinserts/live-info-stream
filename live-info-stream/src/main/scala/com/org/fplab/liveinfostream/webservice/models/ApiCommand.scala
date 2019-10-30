package com.org.fplab.liveinfostream.webservice.models

import io.circe._
import io.circe.generic.auto._
import io.circe.syntax._

import java.time.Instant
import java.util.concurrent.TimeUnit
import scala.concurrent.duration.FiniteDuration

/** Command to be sent to clients via web socket message */
sealed trait ApiCommand {
  val command: String
  def getJson: Json
  def getAssociatedMarketId: Option[String]
  def getRateLimiter: Option[ApiCommandLimitRate] // None means command does not support rate limiting
}

final case class ServerTimeCommand(
  instant: Instant,
  command: String = "serverTime"
) extends ApiCommand {
  def getJson: Json                               = this.asJson
  def getAssociatedMarketId: Option[String]       = None
  def getRateLimiter: Option[ApiCommandLimitRate] = None
}

final case class TradedVolumeChangedCommand(
  marketId: String,
  tradedVolume: Double,
  command: String = "tradedVolumeChanged"
) extends ApiCommand {
  def getJson: Json                               = this.asJson
  def getAssociatedMarketId: Option[String]       = Option(marketId)
  def getRateLimiter: Option[ApiCommandLimitRate] =
    Some(ApiCommandLimitRate(s"TVC-$marketId", FiniteDuration(1, TimeUnit.SECONDS)))
}

final case class MarketInPlayChangedCommand(
  marketId: String,
  inPlay: Boolean,
  command: String = "inPlayChanged"
) extends ApiCommand {
  def getJson: Json                               = this.asJson
  def getAssociatedMarketId: Option[String]       = Option(marketId)
  def getRateLimiter: Option[ApiCommandLimitRate] = None
}

final case class MarketStatusChangedCommand(
  marketId: String,
  status: String,
  command: String = "marketStatusChanged"
) extends ApiCommand {
  def getJson: Json                               = this.asJson
  def getAssociatedMarketId: Option[String]       = Option(marketId)
  def getRateLimiter: Option[ApiCommandLimitRate] = None
}

final case class RunnerPriceChangedCommand(
  marketId: String,
  runnerId: Long,
  hc: Double,
  price: Double,
  command: String = "runnerPriceChanged"
) extends ApiCommand {
  def getJson: Json                               = this.asJson
  def getAssociatedMarketId: Option[String]       = Option(marketId)
  def getRateLimiter: Option[ApiCommandLimitRate] =
    Some(ApiCommandLimitRate(s"RPC-$marketId-$runnerId", FiniteDuration(500, TimeUnit.MILLISECONDS)))
}

final case class RunnerStatusChangedCommand(
  marketId: String,
  runnerId: Long,
  hc: Double,
  status: String,
  command: String = "runnerStatusChanged"
) extends ApiCommand {
  def getJson: Json                               = this.asJson
  def getAssociatedMarketId: Option[String]       = Option(marketId)
  def getRateLimiter: Option[ApiCommandLimitRate] = None
}

final case class RunnerVolumeChangedCommand(
  marketId: String,
  runnerId: Long,
  hc: Double,
  volume: Double,
  command: String = "runnerVolumeChanged"
) extends ApiCommand {
  def getJson: Json                               = this.asJson
  def getAssociatedMarketId: Option[String]       = Option(marketId)
  def getRateLimiter: Option[ApiCommandLimitRate] =
    Some(ApiCommandLimitRate(s"RVC-$marketId-$runnerId", FiniteDuration(3, TimeUnit.SECONDS)))
}

final case class OnlineUserCountChangedCommand(
  userCount: Int,
  command: String = "onlineUserCountChanged"
) extends ApiCommand {
  def getJson: Json                               = this.asJson
  def getAssociatedMarketId: Option[String]       = None
  def getRateLimiter: Option[ApiCommandLimitRate] = None
}
