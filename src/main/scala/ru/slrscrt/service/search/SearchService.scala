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


  def searchTagInfo(tags: Set[String]): Future[Map[String, TagStatisticInfo]] = {
    httpClient.request(tags)
  }


}
