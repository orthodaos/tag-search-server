package ru.slrscrt.settings

import akka.actor.{ActorContext, ExtendedActorSystem, Extension, ExtensionId, ExtensionIdProvider}
import akka.stream.{ActorMaterializer, ActorMaterializerSettings}
import com.typesafe.config.Config

/**
  * Конфигурационные настройки
  *
  * @author Roman Maksyutov
  */
class Settings private(val config: Config)(implicit extendedSystem: ExtendedActorSystem)
  extends PartialSettings(config) with Extension {

  implicit val materializer: ActorMaterializer = ActorMaterializer(ActorMaterializerSettings(extendedSystem))

  val defaults = new DefaultsSettings("defaults")

  val http = new HttpSettings("http")

  val search = new SearchSettings("search")

}


object Settings extends ExtensionId[Settings] with ExtensionIdProvider {

  override def lookup(): ExtensionId[Settings] = Settings

  override def createExtension(system: ExtendedActorSystem) = new Settings(system.settings.config)(system)

  def apply(context: ActorContext): Settings = apply(context.system)

}
