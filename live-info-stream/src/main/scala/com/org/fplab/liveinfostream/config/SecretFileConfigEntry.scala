package com.org.fplab.liveinfostream.config

import java.nio.file.Paths;

import cats.effect.Blocker
import ciris._

object SecretFileConfigEntry {
  def envSecretFile(key: String)(implicit blocker: Blocker): ConfigValue[String] =
    env(key).flatMap(fileName => file(Paths.get(fileName), blocker)).map(_.trim)
}