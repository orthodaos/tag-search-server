package ru.slrscrt.service

import ru.slrscrt.SystemSupport

/**
  * @author Roman Maksyutov
  */
trait ServicesSupport extends SystemSupport {

  val services: Services = Services(system)

}

