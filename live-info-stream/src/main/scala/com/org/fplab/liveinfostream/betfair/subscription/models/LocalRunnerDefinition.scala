package com.org.fplab.liveinfostream.betfair.subscription.models

import com.betfair.esa.swagger.model.RunnerDefinition

object LocalRunnerDefinition {
  def fromRunnerDefinition(runnerDefinition: RunnerDefinition): LocalRunnerDefinition =
    LocalRunnerDefinition(
      runnerDefinition.getId,
      runnerDefinition.getHc,
      runnerDefinition.getStatus.getValue
    )
}

final case class LocalRunnerDefinition(
  id: Long,
  hc: Double,
  status: String
)
