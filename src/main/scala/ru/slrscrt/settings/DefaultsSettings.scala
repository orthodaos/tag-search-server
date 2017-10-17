package ru.slrscrt.settings

import java.time.ZoneOffset
import java.util.concurrent.TimeUnit

import akka.util.Timeout
import com.typesafe.config.Config

import scala.concurrent.duration.FiniteDuration

/**
  * @author Roman Maksyutov
  */
private[settings] class DefaultsSettings(config: Config) extends PartialSettings(config) {

  val accessTimeout: FiniteDuration = "access-timeout"

  implicit val defaultTimeout: Timeout = accessTimeout

  implicit val timeZoneOffset: ZoneOffset = "time-zone-offset"

  implicit val timeZoneOffsetMillis: Long = TimeUnit.SECONDS.toMillis(timeZoneOffset.getTotalSeconds)

}
