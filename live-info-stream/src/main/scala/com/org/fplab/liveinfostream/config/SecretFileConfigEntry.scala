package com.org.fplab.liveinfostream.config

import java.nio.file.Paths

import ciris._

object SecretFileConfigEntry {
  def envSecretFile(key: String): ConfigValue[Effect, String] =
    env(key).flatMap(fileName => file(Paths.get(fileName))).map(_.trim)
}
