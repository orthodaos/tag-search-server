package ru.slrscrt

import akka.actor.ActorSystem
import ru.slrscrt.http.HttpServer
import ru.slrscrt.util.Logging

/**
  * @author Roman Maksyutov
  */
object Application extends App with Logging {

  implicit val startAt: Long = System.currentTimeMillis

  implicit val system: ActorSystem = ActorSystem("v-banking-actor-system")

  val httpServer: HttpServer = new HttpServer

  httpServer.startServer()

}
