package ru.slrscrt.settings

import ru.slrscrt.SystemSupport

/**
  * @author Roman Maksyutov
  */
trait SettingsSupport extends SystemSupport {

  val settings: Settings = Settings(system)

}
