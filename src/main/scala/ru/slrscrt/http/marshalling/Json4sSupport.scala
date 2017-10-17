package ru.slrscrt.http.marshalling

import java.lang.reflect.InvocationTargetException

import akka.http.scaladsl.marshalling.{Marshaller, ToEntityMarshaller}
import akka.http.scaladsl.model.MediaTypes.`application/json`
import akka.http.scaladsl.unmarshalling.{FromEntityUnmarshaller, Unmarshaller}
import akka.util.ByteString
import org.json4s.{Formats, MappingException, Serialization}
import ru.slrscrt.util.Logging

import scala.util.{Failure, Success, Try}

/**
  * @author Roman Maksyutov
  */
trait Json4sSupport extends Logging {

  private val jsonStringUnmarshaller: FromEntityUnmarshaller[String] =
    Unmarshaller.byteStringUnmarshaller
      .forContentTypes(`application/json`)
      .mapWithCharset {

        case (ByteString.empty, _) =>
          throw Unmarshaller.NoContentException

        case (data, charset) =>
          data.decodeString(charset.nioCharset.name)
      }

  implicit val jsonStringMarshaller: ToEntityMarshaller[String] =
    Marshaller.stringMarshaller(`application/json`)

  /**
    * HTTP entity => `A`
    *
    * @tparam A type to decode
    * @return unmarshaller for `A`
    */
  implicit def json4sUnmarshaller[A: Manifest](
                                                implicit serialization: Serialization,
                                                formats: Formats
                                              ): FromEntityUnmarshaller[A] =
    jsonStringUnmarshaller.map { data =>
      Try(serialization.read(data)) match {

        case Success(res) =>
          res

        case Failure(e) =>
          val Class = implicitly[Manifest[A]].runtimeClass
          logger.error(s"Ошибка десериализации [${Class.getSimpleName}] из: $data", e)
          throw e
      }
    }.recover(
      _ =>
        _ => {
          case MappingException("unknown error",
          ite: InvocationTargetException) =>
            throw ite.getCause
        }
    )


  /**
    * `A` => HTTP entity
    *
    * @tparam A type to encode, must be upper bounded by `AnyRef`
    * @return marshaller for any `A` value
    */
  implicit def json4sMarshaller[A <: AnyRef](
                                              implicit serialization: Serialization,
                                              formats: Formats
                                            ): ToEntityMarshaller[A] =
    jsonStringMarshaller.compose(serialization.writePretty[A])

}
