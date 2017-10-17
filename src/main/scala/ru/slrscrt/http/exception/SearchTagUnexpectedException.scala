package ru.slrscrt.http.exception

/**
  * @author Roman Maksyutov
  */
case class SearchTagUnexpectedException(tag: String)
  extends RuntimeException(s"Непредвиденная ошибка при получении данных по тэгу: [$tag]")