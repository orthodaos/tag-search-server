package ru.slrscrt.service.search

import java.net.URLEncoder
import java.nio.charset.StandardCharsets

import akka.NotUsed
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.coding.{Deflate, Gzip, NoCoding}
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.HttpEncodings
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.http.scaladsl.util.FastFuture
import akka.stream.scaladsl.{Flow, Source}
import ru.slrscrt.http.exception.{SearchTagResponseException, SearchTagUnexpectedException}
import ru.slrscrt.http.marshalling.JsonSupport
import ru.slrscrt.http.model.{SearchTagError, TagItems, TagStatisticInfo}
import ru.slrscrt.service.ExecutionContextSupport
import ru.slrscrt.settings.SettingsSupport

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

/**
  * Http клинет для получения данных по тэгу
  *
  * @author Roman Maksyutov
  */
class SearchHttpClient(implicit val system: ActorSystem) extends JsonSupport
  with SettingsSupport
  with ExecutionContextSupport {

  import settings.materializer
  import settings.search.Connection._

  val connectionPoolFlow: Flow[(HttpRequest, String), (Try[HttpResponse], String), Http.HostConnectionPool] =
    protocol match {
      case "https" =>
        Http().cachedHostConnectionPoolHttps[String](host, port)
      case _ =>
        Http().cachedHostConnectionPool[String](host, port)
    }

  type Response = Map[String, TagStatisticInfo]


  private def encode(p: String): String = URLEncoder.encode(p, StandardCharsets.UTF_8.name)


  private val RequestUrl: String = {
    import settings.search._
    s"$url?pagesize=$pageSize&order=${encode(order)}&sort=${encode(sort)}&site=${encode(site)}&tagged="
  }


  private def createRequest(tag: String): Future[(HttpRequest, String)] = {
    val uri = RequestUrl + encode(tag)
    Future.successful(HttpRequest(HttpMethods.GET, uri) -> tag)
  }

  private val parallelism: Int = settings.search.parallelism


  def request(tags: Set[String]): Future[Response] = {
    Source(tags).mapAsync(parallelism.min(tags.size))(createRequest)
  }


  implicit private def withSource: Source[(HttpRequest, String), NotUsed] => Future[Response] = {

    def decode(response: HttpResponse): HttpEntity = {
      (response.encoding match {
        case HttpEncodings.gzip =>
          Gzip
        case HttpEncodings.deflate =>
          Deflate
        case _ =>
          NoCoding
      }).decodeMessage(response).entity
    }

    _.log("Search Request").via(connectionPoolFlow).log("Search Response").runFoldAsync(Map[String, TagStatisticInfo]()) {

      case (map, (Success(resp@(HttpResponse(StatusCodes.OK, _, _, _))), tag)) =>

        val entity: HttpEntity = decode(resp)
        Unmarshal(entity).to[TagItems] transformWith {
          case Success(result) =>
            //TODO Возможно не совсем верно понял, как подсчитывать статистику
            // и с точки зрения производительности оптимальнее сделать foldLeft, но так более читабельно
            val total = result.items.filter(_.tags.contains(tag))
            val answered = total.count(_.is_answered)

            FastFuture.successful(map + (tag -> TagStatisticInfo(total.size, answered)))


          case Failure(e) =>
            Unmarshal(entity).to[SearchTagError] map { error =>
              logger.error(s"Произошла ошибка получения данных по тэгу: [$tag]", e)
              throw SearchTagResponseException(tag, Some(error))
            }
        }

      case (_, (Success(resp), tag)) =>
        logger.error(s"Произошла ошибка получения данных по тэгу: [$tag]")
        Unmarshal(decode(resp)).to[SearchTagError] map { error =>
          throw SearchTagResponseException(tag, Some(error))
        }

      case (_, (Failure(e), tag)) =>
        logger.error(s"Произошла непредвиденная ошибка при получении данных по тэгу: [$tag]", e)
        throw SearchTagUnexpectedException(tag)
    }
  }

}
