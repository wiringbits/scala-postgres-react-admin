package net.wiringbits.spra.api.models

import play.api.libs.json.{Format, Json, Reads, Writes}

object AdminUpdateTable {
  case class Request(data: Map[String, String])
  case class Response(id: String)

  implicit val adminUpdateTableRequestFormat: Format[Request] = Format[Request](
    // We have to handle null as Options
    fjs = implicitly[Reads[Map[String, Option[String]]]]
      .map(handleMapWithOptionalValue)
      .map(Request.apply),
    tjs = Writes[Request](x => Json.toJson(x.data))
  )

  implicit val adminUpdateTableResponseFormat: Format[Response] =
    Json.format[Response]
}
