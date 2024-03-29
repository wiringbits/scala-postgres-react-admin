package net.wiringbits.spra.admin

import net.wiringbits.spra.admin.utils.models.QueryParameters
import play.api.libs.json.Json

package object utils {
  implicit class StringToDataTypesExt(val str: String) extends AnyVal {
    // convert ["id", "ASC"] string to List("id", "ASC")
    implicit def toStringList: List[String] = {
      str.substring(1, str.length - 1).filterNot(x => x == '"').split(",").toList
    }

    // convert json object string (for example: "{}") to Map
    implicit def toStringMap: Map[String, String] = {
      val maybe = Json.parse(str).validate[Map[String, String]]
      maybe.getOrElse(Map.empty)
    }
  }

  def contentRangeHeader(tableName: String, queryParameters: QueryParameters, numberOfRecords: Int): String = {
    s"$tableName ${queryParameters.pagination.start}-${queryParameters.pagination.end}/$numberOfRecords"
  }
}
