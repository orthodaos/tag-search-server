package ru.slrscrt.settings

import com.typesafe.config.Config

/**
  * @author Roman Maksyutov
  */
private[settings] class HttpSettings(config: Config) extends PartialSettings(config) {

  val host: String = "host".str

  val port: Int = "port"

}
