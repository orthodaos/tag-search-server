package ru.slrscrt.settings

import java.time.ZoneOffset

import com.typesafe.config.Config

import scala.concurrent.duration.FiniteDuration

/**
  * @author Roman Maksyutov
  */
class PartialSettings private[settings](config: Config) {

  import ru.slrscrt.util.TimeUtils.TimeParser

  implicit def path2ZoneOffset(path: String): ZoneOffset = path.zoneOffset

  implicit def path2Int(path: String): Int = path.int

  implicit def path2Duration(path: String): FiniteDuration = path.duration

  implicit def path2Conf(path: String): Config = path.conf

  implicit def path2Boolean(path: String): Boolean = path.bool


  implicit class PathExt(path: String) {

    def str: String = config.getString(path)

    def int: Int = config.getInt(path)

    def duration: FiniteDuration = path.str.toDuration

    def zoneOffset: ZoneOffset = ZoneOffset.of(path.str)

    def conf: Config = config.getConfig(path)

    def bool: Boolean = config.getBoolean(path)
  }


}