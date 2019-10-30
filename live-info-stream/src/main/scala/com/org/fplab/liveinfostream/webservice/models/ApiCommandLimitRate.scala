package com.org.fplab.liveinfostream.webservice.models

import scala.concurrent.duration.FiniteDuration

final case class ApiCommandLimitRate(key: String, rate: FiniteDuration)
