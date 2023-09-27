package net.wiringbits.spra.api.models

import play.api.libs.json.{Format, Json, Reads, Writes}

object AdminUpdateTable {
  case class Request(data: Map[String, String])
  case class Response(id: String)

  implicit val adminUpdateTableRequestFormat: Format[Request] = Format[Request](
    fjs = implicitly[Reads[Map[String, String]]].map(Request.apply),
    tjs = Writes[Request](x => Json.toJson(x.data))
  )

  implicit val adminUpdateTableResponseFormat: Format[Response] =
    Json.format[Response]
}
