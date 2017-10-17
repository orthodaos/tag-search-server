package ru.slrscrt.service

import akka.actor.{ActorContext, ExtendedActorSystem, Extension, ExtensionId, ExtensionIdProvider}
import ru.slrscrt.service.search.SearchService

/**
  * @author Roman Maksyutov
  */
class Services private(implicit extendedSystem: ExtendedActorSystem) extends Extension {

  val searchService = new SearchService()

}


object Services extends ExtensionId[Services] with ExtensionIdProvider {

  override def lookup(): ExtensionId[Services] = Services

  override def createExtension(system: ExtendedActorSystem) = new Services()(system)

  def apply(context: ActorContext): Services = apply(context.system)

}
