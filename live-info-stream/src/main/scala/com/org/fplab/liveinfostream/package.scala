package com.org.fplab

import cats.mtl.ApplicativeAsk
import com.org.fplab.liveinfostream.config.AppConfiguration

package object liveinfostream {
  type ConfigurationAsk[F[_]] = ApplicativeAsk[F, AppConfiguration]
}
