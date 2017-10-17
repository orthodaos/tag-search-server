package ru.slrscrt.http.exception

import ru.slrscrt.http.model.SearchTagError

/**
  * @author Roman Maksyutov
  */
case class SearchTagResponseException(tag: String, error: Option[SearchTagError] = None)
  extends RuntimeException(s"Ошибка получения данных по тэгу: [$tag]: ${error.getOrElse("-")}")