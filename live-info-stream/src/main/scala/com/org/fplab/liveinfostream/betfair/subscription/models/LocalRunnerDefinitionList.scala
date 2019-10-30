package com.org.fplab.liveinfostream.betfair.subscription.models

import com.betfair.esa.swagger.model.RunnerDefinition

import scala.jdk.CollectionConverters._

object LocalRunnerDefinitionList {
  def fromRunnerDefinitionList(items: java.util.List[RunnerDefinition]): LocalRunnerDefinitionList =
    LocalRunnerDefinitionList(
      items.asScala.toList.map(LocalRunnerDefinition.fromRunnerDefinition(_))
    )
}

case class LocalRunnerDefinitionList(items: List[LocalRunnerDefinition])
