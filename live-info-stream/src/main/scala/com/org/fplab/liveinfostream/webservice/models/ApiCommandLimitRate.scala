package com.org.fplab.liveinfostream.webservice.models

import upperbound.Rate

final case class ApiCommandLimitRate(key: String, rate: Rate)
