package ru.slrscrt.util

import java.util.concurrent.TimeUnit

import akka.http.scaladsl.model.DateTime

import scala.annotation.tailrec
import scala.concurrent.duration.{FiniteDuration, _}
import scala.language.postfixOps
import scala.util.Try
import scala.util.matching.Regex.Match

/**
  * @author Roman Maksyutov
  */
object TimeUtils {


  implicit class TimeParser(str: String) {

    /**
      * Convert text like '100 ms' or '100 sec' to time in milliseconds.
      * If the time unit is not specified then is taken milliseconds by default
      */
    def toMillisTime: Long = Try {

      def parseTimeUnit: String => TimeUnit = {
        case "ms" | "milli" | "millis" | "millisecond" | "milliseconds" | "" => TimeUnit.MILLISECONDS
        case "s" | "sec" | "second" | "seconds" => TimeUnit.SECONDS
        case "m" | "min" | "minute" | "minutes" => TimeUnit.MINUTES
        case "h" | "hour" | "hours" => TimeUnit.HOURS
        case "d" | "day" | "days" => TimeUnit.DAYS
      }

      @tailrec def parseTime(matches: List[Match], time: Long): Long = matches match {
        case Nil =>
          time
        case head :: tail =>
          parseTime(tail, time + parseTimeUnit(head.group(2)).toMillis(head.group(1).toLong))
      }

      parseTime("(\\d+)\\s*(\\w*)".r.findAllMatchIn(str).toList, 0)

    } getOrElse (throw new IllegalArgumentException(s"Wrong time format for: '$str]'"))

    def toDuration: FiniteDuration = toMillisTime millis

    def toDateTime: DateTime = DateTime(toMillisTime)

  }

}
