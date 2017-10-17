package ru.slrscrt.util

import java.util.concurrent.TimeUnit

import scala.concurrent.duration.FiniteDuration

/**
  * @author Roman Maksyutov
  */
object DateTimeUtils {

  implicit class DurationExt(duration: FiniteDuration) {

    def description: String = {

      implicit class SbExt(sb: StringBuilder) {

        def appendNonZero(v: Long, unit: String): StringBuilder = if (v > 0) {
          appendTime(v, unit)
        } else sb

        def appendTime(v: Long, unit: String): StringBuilder = {
          sb.append(v).append(" ").append(unit).append(" ")
        }
      }

      var d: FiniteDuration = duration
      val days = d.toDays
      d -= FiniteDuration(days, TimeUnit.DAYS)
      val hours = d.toHours
      d -= FiniteDuration(hours, TimeUnit.HOURS)
      val minutes = d.toMinutes
      d -= FiniteDuration(minutes, TimeUnit.MINUTES)
      val seconds = d.toSeconds
      d -= FiniteDuration(seconds, TimeUnit.SECONDS)
      val millis = d.toMillis
      d -= FiniteDuration(millis, TimeUnit.MILLISECONDS)
      val nanos = d.toNanos
      new StringBuilder()
        .appendNonZero(days, "days")
        .appendNonZero(hours, "hours")
        .appendNonZero(minutes, "min")
        .appendTime(seconds, "sec")
        .appendNonZero(millis, "ms")
        .appendNonZero(nanos, "ns")
        .toString
    }

  }

}
