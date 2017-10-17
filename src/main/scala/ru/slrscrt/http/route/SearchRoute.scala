package ru.slrscrt.http.route

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Route
import ru.slrscrt.service.ServicesSupport

/**
  * @author Roman Maksyutov
  */
class SearchRoute(implicit val system: ActorSystem) extends BaseRoute with ServicesSupport {

  import akka.http.scaladsl.server.Directives._

  val route: Route = postOrGet {
    path("search") {
      decodeRequest {
        parameters('tag.*) { tags =>
          services.searchService.searchTagInfo(tags.toSet)
        }
      }
    }
  }


}
