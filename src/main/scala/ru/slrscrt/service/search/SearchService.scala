package ru.slrscrt.service.search

import akka.actor.ActorSystem
import ru.slrscrt.http.model.TagStatisticInfo
import ru.slrscrt.service.Service

import scala.concurrent.Future

/**
  * @author Roman Maksyutov
  */
class SearchService(implicit val system: ActorSystem) extends Service {

  val httpClient = new SearchHttpClient


  /**
    * Поиск и обработка информации по тэгам
    *
    * @param tags - тэги по которым искать статистику
    * @return Статистику по каждома тэгу
    */
  def searchTagInfo(tags: Set[String]): Future[Map[String, TagStatisticInfo]] = {
    httpClient.executeSearchRequests(tags) {
      case (tag, result) =>
        //TODO Возможно не совсем верно понял, как подсчитывать статистику
        // и с точки зрения производительности оптимальнее сделать foldLeft, но так более читабельно
        val total = result.items.filter(_.tags.contains(tag))
        val answered = total.count(_.is_answered)
        tag -> TagStatisticInfo(total.size, answered)
    }
  }


}
