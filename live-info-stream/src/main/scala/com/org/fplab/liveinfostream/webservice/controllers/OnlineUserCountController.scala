package com.org.fplab.liveinfostream.webservice.controllers

import cats.implicits._
import cats.data.State
import cats.effect.{Concurrent, Sync}
import cats.effect.concurrent.Ref
import com.org.fplab.liveinfostream.state.ApplicationState
import com.org.fplab.liveinfostream.webservice.models.{ApiCommand, OnlineUserCountChangedCommand}
import com.org.fplab.liveinfostream.webservice.state.WebServiceState
import fs2.concurrent.Topic

class OnlineUserCountController[F[_]: Sync : Concurrent](stateRef: Ref[F, ApplicationState[F]], topic: Topic[F, Option[String]]) {
  def addUser: F[ApiCommand] = updateStateAndPublishCommand(_ + 1)

  def removeUser: F[ApiCommand] = updateStateAndPublishCommand(_ - 1)

  private def updateState(updateUserCount: Int => Int): State[ApplicationState[F], ApiCommand] = for {
    _ <- State.modify((ApplicationState.webService[F] composeLens WebServiceState.onlineUserCount).modify(updateUserCount))
    userCount <- State.inspect[ApplicationState[F], Int](_.webService.onlineUserCount)
  } yield OnlineUserCountChangedCommand(userCount)

  def updateStateAndPublishCommand(updateUserCount: Int => Int): F[ApiCommand] = for {
    command <- stateRef.modifyState(updateState(updateUserCount))
    _ <- topic.publish1(Some(command.getJson.noSpaces))
  } yield command

}
