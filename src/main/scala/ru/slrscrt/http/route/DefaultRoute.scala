package ru.slrscrt.http.route

import akka.http.scaladsl.server.Route

/**
  * @author Roman Maksyutov
  */
object DefaultRoute extends BaseRoute {

  import akka.http.scaladsl.server.Directives._

  val route: Route = postOrGet {
    pathSingleSlash {
      ok("Server is working")
    }
  }

}
