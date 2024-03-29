package net.wiringbits.spra.admin.models

import net.wiringbits.spra.admin.utils.models.SortParameter
import org.scalatest.matchers.must.Matchers.{be, must}
import org.scalatest.wordspec.AnyWordSpec

class SortParameterSpec extends AnyWordSpec {
  "fromString" should {
    "create a valid sort parameter" in {
      val str = """["name","ASC"]"""
      // val primaryKeyField = "user_id"
      val response = SortParameter.fromString(str)
      response.field must be("name")
      response.ordering must be("ASC")
    }
  }
}
