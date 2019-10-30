package com.org.fplab.liveinfostream.utils

import java.time.Instant

import org.threeten.bp.OffsetDateTime

object DateTimeUtils {
  def threeTenToInstant(d: OffsetDateTime): Instant =
    Instant.ofEpochSecond(d.toEpochSecond)
}
