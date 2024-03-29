package net.wiringbits.spra.api.models

import play.api.libs.json.{Format, Json}

object AdminDeleteTable {
  case class Response(noData: String = "")

  implicit val adminDeleteTableResponseFormat: Format[Response] =
    Json.format[Response]
}
