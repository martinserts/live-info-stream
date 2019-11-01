package com.org.fplab.liveinfostream.webservice.core

import cats.implicits._
import cats.data.Chain
import com.org.fplab.liveinfostream.betfair.subscription.models.LocalMarket
import com.org.fplab.liveinfostream.webservice.models.{ApiCommand, GuiMarket, GuiRunner, MarketInPlayChangedCommand, MarketStatusChangedCommand, RunnerPriceChangedCommand, RunnerStatusChangedCommand, RunnerVolumeChangedCommand, TradedVolumeChangedCommand}

object MarketChangeExtractor {
  /** Compares two local markets and returns list of changes in form of ApiCommands */
  def getStateChanges(oldState: LocalMarket, newState: LocalMarket): Option[List[ApiCommand]] = {
    // We done care about market, runner name changes (I guess they never happen)
    def convertMarket: LocalMarket => GuiMarket = MarketConverter.toGuiMarket(
      Function.const(""),
      Function.const(""),
      Function.const(""))

    val oldMarket = convertMarket(oldState)
    val newMarket = convertMarket(newState)

    val changes = List(
      findTradedVolumeChanges(oldMarket, newMarket),
      findMarketInPlayChanges(oldMarket, newMarket),
      findMarketStatusChanges(oldMarket, newMarket),
      findRunnersChanges(oldMarket, newMarket)
    ).combineAll

    if (changes.isEmpty) None else Some(changes.toList)
  }

  private def findTradedVolumeChanges(oldMarket: GuiMarket, newMarket: GuiMarket): Chain[ApiCommand] = {
    val newTradedVolume = newMarket.tradedVolume
    if (oldMarket.tradedVolume != newTradedVolume)
      Chain.one(TradedVolumeChangedCommand(newMarket.id, newTradedVolume))
    else Chain.empty
  }

  private def findMarketInPlayChanges(oldMarket: GuiMarket, newMarket: GuiMarket): Chain[ApiCommand] = {
    val newInPlay = newMarket.inPlay
    if (oldMarket.inPlay != newInPlay)
      Chain.one(MarketInPlayChangedCommand(newMarket.id, newInPlay))
    else Chain.empty
  }

  private def findMarketStatusChanges(oldMarket: GuiMarket, newMarket: GuiMarket): Chain[ApiCommand] = {
    val newMarketStatus = newMarket.status
    if (oldMarket.status != newMarketStatus)
      Chain.one(MarketStatusChangedCommand(newMarket.id, newMarketStatus))
    else Chain.empty
  }

//  private def findRunnersPriceChanges(oldMarket: GuiMarket, newMarket: GuiMarket): Chain[ApiCommand] = {
//    def findPriceChanges(runners: List[GuiRunner])(runner: GuiRunner): Option[GuiRunner] =
//      runners.find(r => r.id == runner.id && r.hc == runner.hc && r.price != runner.price)
//
//    val findDiffs = findPriceChanges(newMarket.runners)(_)
//    val changes = oldMarket.runners.flatMap(findDiffs)
//      .map(r => RunnerPriceChangedCommand(newMarket.id, r.id, r.hc, r.price))
//
//    if (changes.isEmpty) Chain.empty else Chain.fromSeq(changes)
//  }
//
//  private def findRunnersStatusChanges(oldMarket: GuiMarket, newMarket: GuiMarket): Chain[ApiCommand] = {
//    def findStatusChanges(runners: List[GuiRunner])(runner: GuiRunner): Option[GuiRunner] =
//      runners.find(r => r.id == runner.id && r.hc == runner.hc && r.status != runner.status)
//
//    val findDiffs = findStatusChanges(newMarket.runners)(_)
//    val changes = oldMarket.runners.flatMap(findDiffs)
//      .map(r => RunnerStatusChangedCommand(newMarket.id, r.id, r.hc, r.status))
//
//    if (changes.isEmpty) Chain.empty else Chain.fromSeq(changes)
//  }

  private def findCorrespondingRunner(runners: List[GuiRunner])(runner: GuiRunner): Option[GuiRunner] =
    runners.find(r => r.id == runner.id && r.hc == runner.hc)

  private def getRunnerPriceChanges(market: GuiMarket)(a: GuiRunner, b: GuiRunner): Option[ApiCommand] = {
    if (a.price != b.price) Some(RunnerPriceChangedCommand(market.id, b.id, b.hc, b.price)) else None
  }

  private def getRunnerStatusChanges(market: GuiMarket)(a: GuiRunner, b: GuiRunner): Option[ApiCommand] = {
    if (a.status != b.status) Some(RunnerStatusChangedCommand(market.id, b.id, b.hc, b.status)) else None
  }

  private def getRunnerVolumeChanges(market: GuiMarket)(a: GuiRunner, b: GuiRunner): Option[ApiCommand] = {
    if (a.volume != b.volume) Some(RunnerVolumeChangedCommand(market.id, b.id, b.hc, b.volume)) else None
  }

  private def extractRunnerChanges(market: GuiMarket)(a: GuiRunner, b: GuiRunner): List[ApiCommand] = {
    List(
      getRunnerPriceChanges(market)(a, b),
      getRunnerStatusChanges(market)(a, b),
      getRunnerVolumeChanges(market)(a, b)
    ).flatten
  }

  private def findRunnersChanges(oldMarket: GuiMarket, newMarket: GuiMarket): Chain[ApiCommand] = {
    val changes = newMarket.runners
      .flatMap(newRunner =>
        findCorrespondingRunner(oldMarket.runners)(newRunner).
          flatMap(oldRunner => Some(extractRunnerChanges(newMarket)(oldRunner, newRunner))))
      .flatten
    if (changes.isEmpty) Chain.empty else Chain.fromSeq(changes)
  }
}
