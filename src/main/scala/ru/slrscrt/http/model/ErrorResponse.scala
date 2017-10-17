package ru.slrscrt.http.model

/**
  * @author Roman Maksyutov
  */
case class ErrorResponse(errorCode: Int,
                         errorMessage: String,
                         errorData: Option[Any] = None)

object ErrorResponse {

  def apply(e: Throwable) = new ErrorResponse(500, s"Ошибка обработки запроса: $e", Some(e))

  def apply(errorCode: Int, errorMessage: String) = new ErrorResponse(errorCode, errorMessage)

}
