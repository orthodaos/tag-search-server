package ru.slrscrt.http.route

import akka.http.scaladsl.server.Route
import ru.slrscrt.http.marshalling.JsonSupport
import ru.slrscrt.http.model.ErrorResponse
import ru.slrscrt.util.Logging

import scala.concurrent.Future

/**
  * @author Roman Maksyutov
  */
trait BaseRoute extends Route with JsonSupport with Logging {

  import akka.http.scaladsl.model._
  import StatusCodes.{BadRequest, InternalServerError, OK}
  import akka.http.scaladsl.model.HttpEntity
  import akka.http.scaladsl.server.Directives._
  import akka.http.scaladsl.server._

  val route: Route


  override def apply(ctx: RequestContext): Future[RouteResult] = route(ctx)

  def postOrGet: Directive0 = post | get

  val EmptyResponse: HttpResponse = HttpResponse(status = OK, entity = HttpEntity.Empty)

  val EmptyOk: StandardRoute = complete(EmptyResponse)


  def ok: Any => Route = {

    case _: Unit =>
      EmptyOk

    case e: Throwable =>
      failServer(e)

    case response: AnyRef =>
      complete(OK, response)
  }


  def failRequest(msg: String): Route = {
    logger.error("Некорректный запрос:", msg)
    complete(BadRequest, ErrorResponse(400, s"Некорректный запрос: $msg"))
  }

  def failServer(e: Throwable): Route = {
    logger.error("Ошибка обработки запроса:", e)
    complete(InternalServerError, ErrorResponse(e))
  }


  implicit def _toRoute[B](f: Future[B]): Route = {
    onSuccess(f)(ok(_))
  }


}
