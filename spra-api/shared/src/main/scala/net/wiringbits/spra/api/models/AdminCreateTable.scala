package net.wiringbits.spra.api.models

import play.api.libs.json.{Format, Json, Reads, Writes}

object AdminCreateTable {
  case class Request(data: Map[String, String])

  case class Response(noData: String = "")

  implicit val adminCreateTableRequestFormat: Format[Request] = Format[Request](
    fjs = implicitly[Reads[Map[String, String]]].map(Request.apply),
    tjs = Writes[Request](x => Json.toJson(x.data))
  )

  implicit val adminCreateTableResponseFormat: Format[Response] =
    Json.format[Response]

}
