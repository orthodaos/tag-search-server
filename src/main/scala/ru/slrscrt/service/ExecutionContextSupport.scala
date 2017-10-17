package ru.slrscrt.service

import ru.slrscrt.SystemSupport

import scala.concurrent.ExecutionContextExecutor

/**
  * @author Roman Maksyutov
  */
trait ExecutionContextSupport extends SystemSupport {

  implicit val dispatcher: ExecutionContextExecutor = system.dispatcher

}
