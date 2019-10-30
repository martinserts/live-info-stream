package com.org.fplab.liveinfostream.betfair.subscription.limiter

import cats.effect.testkit.TestControl
import com.org.fplab.liveinfostream.UnitSpec
import com.org.fplab.liveinfostream.betfair.subscription.limiter.RateLimiter.RateLimiterRegistry
import com.org.fplab.liveinfostream.webservice.models.ApiCommandLimitRate
import cats.effect.{IO, Ref}
import cats.implicits._

import scala.concurrent.duration._
import scala.util.Success


class RateLimiterSpec extends UnitSpec {
  /*
  "RateLimiter" should "pass events, if rate is lower than configured" in {
    implicit val runtime = cats.effect.unsafe.IORuntime.global

    val success = testRateLimiter(
      1.second,      // Configured rate
      0.millisecond, // Initial sleep before first event
      500.millisecond,
      600.millisecond
    )
    val result = TestControl.execute(success)
      .flatMap(control => for {
        _ <- control.tick
        _ <- control.advanceAndTick(10.seconds)
        result <- control.results
      } yield result)

    val f = success.unsafeToFuture()
//    ec.tick(10.seconds)
//
    assertResult(Some(Success(true)))(f.value)
  }

  "RateLimiter" should "drop events, if rate is higher than configured" in {
    implicit val ec                   = TestContext()
    implicit val cs: ContextShift[IO] = ec.ioContextShift
    implicit val timer: Timer[IO]     = ec.ioTimer

    val success = testRateLimiter(
      1.second,      // Configured rate
      0.millisecond, // Initial sleep before first event
      500.millisecond,
      10.millisecond // This is too fast
    )
    val f = success.unsafeToFuture()
    ec.tick(10.seconds)

    assertResult(Some(Success(false)))(f.value)
  }
   */

  /** Tests a rate limiter
    *
    * @param expectedLimitRate the expected rate
    * @param fireIntervals a sequence of intervals to fire events (first interval is before initial event)
    */
  def testRateLimiter(
    expectedLimitRate: FiniteDuration,
    fireIntervals: FiniteDuration*
  ): IO[Boolean] =
    for {
      registry    <- Ref.of[IO, RateLimiterRegistry[IO]](Map.empty)
      // Api command configured with expected limit rate
      cmdLimitRate = ApiCommandLimitRate("key", expectedLimitRate)
      // Start rate limiter in background
      rl          <- RateLimiter.launch[IO](cmdLimitRate, None, registry, 1)
      // fire events described by fireIntervals (sleep1, fire1, sleep2, fire2, ...)
      results     <- fireIntervals.toList.map(IO.sleep(_) *> fireEvent(rl)).sequence
      // Stop the rate limiter
      _           <- rl.interrupter.set(true)
    } yield results.forall(_ == true)

  /** Fires event to rate limiter
    *
    * @param rl the rate limiter
    * @return true, if rate limiter did not reject the event
    */
  def fireEvent(rl: RateLimiter[IO]): IO[Boolean] =
    rl.limiter
      .submit(IO.unit)
      .map(Function.const(true))
      .handleErrorWith(Function.const(IO(false)))
}
