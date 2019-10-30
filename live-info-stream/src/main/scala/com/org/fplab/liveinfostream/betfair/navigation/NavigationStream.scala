package com.org.fplab.liveinfostream.betfair.navigation

/** Processes Betfair navigation (tree like) information */

import cats.data._
import cats.implicits._
import cats.effect._
import cats.effect.concurrent._
import com.org.fplab.liveinfostream._
import com.org.fplab.liveinfostream.betfair.navigation.models.NavigationRoot
import com.org.fplab.liveinfostream.betfair.navigation.state.NavigationState
import com.org.fplab.liveinfostream.state.ApplicationState
import fs2._
import fs2.concurrent._
import scala.concurrent.duration._

object NavigationStream {
  /** Fetches Betfair navigation data hourly and saves into application state */
  def createNavigationAutoUpdateStream[F[_]: Sync : ConcurrentEffect : Timer : ConfigurationAsk]
  (interrupter: SignallingRef[F, Boolean], stateRef: Ref[F, ApplicationState], sessionId: String): Stream[F, Unit] =  {
    val navigationUpdateStream = Stream.eval(for {
      data <- NavigationRoot.fromUri(sessionId)
      _ <- saveNavigationData(data, stateRef)
    } yield ())

    (navigationUpdateStream ++ Stream.sleep(1.hour)).repeat
      .interruptWhen(interrupter)
  }

  private def saveNavigationData[F[_]](data: NavigationRoot, stateRef: Ref[F, ApplicationState]): F[Unit] =
    stateRef.modifyState(updateNavigationState(data))

  private def updateNavigationState(data: NavigationRoot): State[ApplicationState, Unit] = {
    val navigationRootLens = ApplicationState.navigation composeOptional NavigationState.root
    State.modify(navigationRootLens.set(data)(_))
  }
}
