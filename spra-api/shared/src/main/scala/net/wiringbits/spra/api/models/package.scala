package net.wiringbits.spra.api

import play.api.libs.json.*

import java.time.Instant
import scala.language.implicitConversions

package object models {

  /** For some reason, play-json doesn't provide support for Instant in the scalajs version, grabbing the jvm values
    * seems to work:
    *   - https://github.com/playframework/play-json/blob/master/play-json/jvm/src/main/scala/play/api/libs/json/EnvReads.scala
    *   - https://github.com/playframework/play-json/blob/master/play-json/jvm/src/main/scala/play/api/libs/json/EnvWrites.scala
    */
  implicit val instantFormat: Format[Instant] = Format[Instant](
    fjs = implicitly[Reads[String]].map(string => Instant.parse(string)),
    tjs = Writes[Instant](i => JsString(i.toString))
  )

  case class ErrorResponse(error: String)
  implicit val errorResponseFormat: Format[ErrorResponse] = Json.format[ErrorResponse]

  implicit def optionFormat[T](using formatter: Format[T]): Format[Option[T]] = Format[Option[T]](
    fjs = Reads[Option[T]](_.validateOpt[T]),
    tjs = Writes[Option[T]] {
      case Some(value) => Json.toJson(value)
      case None => Json.toJson("")
    }
  )

  def handleMapWithOptionalValue(map: Map[String, Option[String]]): Map[String, String] =
    map.collect { case (key, Some(value)) => key -> value }
}
