package com.org.fplab.liveinfostream.betfair.navigation

/** Processes Betfair navigation (tree like) information */

import cats.data._
import cats.effect._
import cats.implicits._
import com.org.fplab.liveinfostream._
import com.org.fplab.liveinfostream.betfair.navigation.models.NavigationRoot
import com.org.fplab.liveinfostream.betfair.navigation.state.NavigationState
import com.org.fplab.liveinfostream.state.ApplicationState
import fs2._
import fs2.concurrent._

import scala.concurrent.duration._

object NavigationStream {
  private val logger = org.log4s.getLogger

  /** Fetches Betfair navigation data hourly and saves into application state */
  def createNavigationAutoUpdateStream[F[_]: Async: ConfigurationAsk](
    interrupter: SignallingRef[F, Boolean],
    stateRef: Ref[F, ApplicationState[F]],
    sessionIdReader: => F[String]
  ): Stream[F, Unit] = {
    val navigationUpdate = for {
      sessionId <- sessionIdReader
      result    <- NavigationRoot.fromUri(sessionId).attempt
      _         <- result match {
                     case Right(navigationRoot) => saveNavigationData(navigationRoot, stateRef)
                     case Left(error)           => Async[F].delay { logger.error(error)("Failed to fetch navigation data") }
                   }
    } yield ()

    (Stream.eval(navigationUpdate) ++ Stream.sleep(1.hour)).repeat
      .interruptWhen(interrupter)
  }

  private def saveNavigationData[F[_]](data: NavigationRoot, stateRef: Ref[F, ApplicationState[F]]): F[Unit] =
    stateRef.modifyState(updateNavigationState(data))

  private def updateNavigationState[F[_]](data: NavigationRoot): State[ApplicationState[F], Unit] = {
    val navigationRootLens = ApplicationState.navigation[F] composeOptional NavigationState.root
    State.modify(navigationRootLens.set(data)(_))
  }
}
