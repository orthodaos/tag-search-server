package ru.slrscrt

import akka.actor.ActorSystem

/**
  * @author Roman Maksyutov
  */
trait SystemSupport {

  implicit val system: ActorSystem

}
