package net.wiringbits.spra.api.models

import play.api.libs.json.{Format, Json}

object AdminCreateTable {
  case class Request(data: Map[String, String])

  case class Response(id: String)

  implicit val adminCreateTableRequestFormat: Format[Request] =
    Json.format[Request]

  implicit val adminCreateTableResponseFormat: Format[Response] =
    Json.format[Response]

}
