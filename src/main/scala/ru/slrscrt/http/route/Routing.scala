package ru.slrscrt.http.route

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.server.Directives.extractUri
import akka.http.scaladsl.server.directives.DebuggingDirectives
import akka.http.scaladsl.server.{ExceptionHandler, RejectionHandler, Route}
import ru.slrscrt.service.ServicesSupport

/**
  * @author Roman Maksyutov
  */
class Routing(implicit val system: ActorSystem) extends BaseRoute with ServicesSupport {


  implicit val exceptionHandler: ExceptionHandler = ExceptionHandler {
    case e =>
      failServer(e)
  }

  implicit val rejectionHandler: RejectionHandler =
    RejectionHandler.newBuilder()
      .handle {
        case rejection =>
          failRequest(s"Некорректный запрос: [$rejection]")

      }.handleNotFound {
      extractUri { uri =>
        failRequest(s"Неизвестный запрос: [${uri.path}]")

      }
    }.result()


  override val route: Route = {
    import akka.http.scaladsl.server.Directives._

    val LoggedRequest: Route = DebuggingDirectives.logRequest("[HTTP Request]", Logging.InfoLevel)(new SearchRoute)
    val LoggedRoute: Route = DebuggingDirectives.logResult("[HTTP Response]", Logging.InfoLevel)(LoggedRequest)

    Route.seal(DefaultRoute ~ LoggedRoute)
  }
}
