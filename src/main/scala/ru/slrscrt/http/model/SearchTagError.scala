package ru.slrscrt.http.model

/**
  * Ошибка которую возвращает API Stack Overflow
  *
  * @author Roman Maksyutov
  */
case class SearchTagError(
                           error_id: Int,
                           error_message: String,
                           error_name: String
                         )
