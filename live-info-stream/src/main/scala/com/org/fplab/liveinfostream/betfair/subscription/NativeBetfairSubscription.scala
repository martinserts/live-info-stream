package com.org.fplab.liveinfostream.betfair.subscription

/** Provides subscription service to Betfair using their own open source implementation in Java
  * We are leaving this code as NOT referentially transparent, since Client, ClientCache are mutating
  */

import cats.implicits._
import cats.effect.{Resource, Sync}
import com.betfair.esa.client.auth.AppKeyAndSessionProvider
import com.betfair.esa.client.cache.market.{MarketChangeEvent, MarketChangeListener, MarketSnap}
import com.betfair.esa.client.{Client, ClientCache}
import com.betfair.esa.swagger.model.MarketFilter
import com.org.fplab.liveinfostream.ConfigurationAsk

object NativeBetfairSubscription {

  /** Subscription resource, that will stop connection of application stop */
  def make[F[_]: Sync](
    callback: (MarketSnap) => Unit
  )(implicit
    C: ConfigurationAsk[F]
  ): Resource[F, NativeBetfairSubscription] =
    Resource.make {
      C.reader(_.betfair) flatMap
        (config =>
          Sync[F].delay {
            val s = new NativeBetfairSubscription(callback)
            s.start(config.appKey.value, config.username.value, config.password.value)
            s
          }
        )
    } { s => Sync[F].delay(s.stop()) }
}

class NativeBetfairSubscription(onMarketChange: (MarketSnap) => Unit) extends MarketChangeListener {
  val HostName    = "stream-api-integration.betfair.com"
  val Port        = 443
  val HorseRacing = "7"

  private var sessionProvider: AppKeyAndSessionProvider = _
  private var client: Client                            = _
  private var clientCache: ClientCache                  = _

  override def marketChange(marketChangeEvent: MarketChangeEvent): Unit =
    onMarketChange(marketChangeEvent.getSnap)

  def getSessionId: String = sessionProvider.getOrCreateNewSession().getSession

  /** Starts connection
    *
    * @param appKey Betfair application key
    * @param username Betfair username
    * @param password Betfair password
    */
  private def start(
    appKey: String,
    username: String,
    password: String
  ): Unit = {
    sessionProvider = new AppKeyAndSessionProvider(AppKeyAndSessionProvider.SSO_HOST_COM, appKey, username, password)

    client = new Client(HostName, Port, sessionProvider)
    clientCache = new ClientCache(client)
    clientCache.getMarketCache.addMarketChangeListener(this)
    val filter = new MarketFilter()
    filter.addEventTypeIdsItem(HorseRacing)
    clientCache.subscribeMarkets(filter)

    client.start()
  }

  /** Stops connection */
  private def stop(): Unit =
    client.stop()
}
