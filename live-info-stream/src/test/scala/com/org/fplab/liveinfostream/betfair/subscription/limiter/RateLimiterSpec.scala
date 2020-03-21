package com.org.fplab.liveinfostream.betfair.subscription.limiter

import com.org.fplab.liveinfostream.UnitSpec
import com.org.fplab.liveinfostream.betfair.subscription.limiter.RateLimiter.RateLimiterRegistry
import com.org.fplab.liveinfostream.webservice.models.ApiCommandLimitRate
import cats.effect.{ContextShift, IO, Timer}
import cats.effect.concurrent.Ref
import cats.effect.laws.util._
import upperbound.Rate
import cats.implicits._

import scala.concurrent.duration._
import scala.util.Success

class RateLimiterSpec extends UnitSpec {
  "RateLimiter" should "pass events, if rate is lower than configured" in {
    implicit val ec = TestContext()
    implicit val cs: ContextShift[IO] = ec.ioContextShift
    implicit val timer: Timer[IO] = ec.ioTimer

    val success = testRateLimiter(1.second, // Configured rate
      0.millisecond, // Initial sleep before first event
      500.millisecond,
      600.millisecond
    )
    val f = success.unsafeToFuture()
    ec.tick(10.seconds)

    assertResult(Some(Success(true)))(f.value)
  }

  "RateLimiter" should "drop events, if rate is higher than configured" in {
      implicit val ec = TestContext()
      implicit val cs: ContextShift[IO] = ec.ioContextShift
      implicit val timer: Timer[IO] = ec.ioTimer

      val success = testRateLimiter(1.second, // Configured rate
        0.millisecond, // Initial sleep before first event
        500.millisecond,
        10.millisecond // This is too fast
      )
      val f = success.unsafeToFuture()
      ec.tick(10.seconds)

      assertResult(Some(Success(false)))(f.value)
    }

  /** Tests a rate limiter
   *
   * @param expectedLimitRate the expected rate
   * @param fireIntervals a sequence of intervals to fire events (first interval is before initial event)
   * */
  def testRateLimiter(expectedLimitRate: FiniteDuration, fireIntervals: FiniteDuration*)
                     (implicit cs: ContextShift[IO], t: Timer[IO]): IO[Boolean] = for {
      registry <- Ref.of[IO, RateLimiterRegistry[IO]](Map.empty)
      // Api command configured with expected limit rate
      cmdLimitRate = ApiCommandLimitRate("key", Rate(1, expectedLimitRate))
      // Start rate limiter in background
      rl <- RateLimiter.launch[IO](cmdLimitRate, None, registry, 1)
      // fire events described by fireIntervals (sleep1, fire1, sleep2, fire2, ...)
      results <- fireIntervals.toList.map(IO.sleep(_) *> fireEvent(rl)).sequence
      // Stop the rate limiter
      _ <- rl.interrupter.set(true)
    } yield results.forall(_ == true)

  /** Fires event to rate limiter
   *
   * @param rl the rate limiter
   * @return true, if rate limiter did not reject the event
   * */
  def fireEvent(rl: RateLimiter[IO]): IO[Boolean] =
    rl.limiter.submit(IO.unit)
      .map(Function.const(true))
      .handleErrorWith(Function.const(IO(false)))
}
