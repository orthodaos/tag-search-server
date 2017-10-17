package ru.slrscrt.http.marshalling

import org.json4s.{DefaultFormats, Formats, Serialization, jackson}

/**
  * @author Roman Maksyutov
  */
trait JsonSupport extends Json4sSupport {

  implicit val serialization: Serialization = jackson.Serialization

  implicit val formats: Formats = DefaultFormats
}