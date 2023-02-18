package net.wiringbits.webapp.utils.admin

import net.wiringbits.webapp.utils.admin.utils.StringRegex
import org.scalatest.matchers.must.Matchers.{be, convertToAnyMustWrapper}
import org.scalatest.wordspec.AnyWordSpec

import java.util.UUID

class StringRegexSpec extends AnyWordSpec {
  "dateRegex" should {
    val dateRegex = StringRegex.dateRegex

    val valid = List(
      "2023-01-20",
      "2012-03-01",
      "2020-09-19",
      "2024-12-31",
      "2002-02-23"
    )

    val invalid = List(
      "aaaa-bb-cc",
      "2022-a3-23",
      "20e1-03-23",
      "2004-01-c4",
      "2012-01-23-a",
      "20230223",
      "??????????",
      "asdfghjkl",
      "ABCDEFGHI"
    )

    valid.foreach { value =>
      s"accept valid value: $value" in {
        dateRegex.matches(value) must be(true)
      }
    }

    invalid.foreach { value =>
      s"reject invalid value: $value" in {
        dateRegex.matches(value) must be(false)
      }
    }
  }

  "uuidRegex" should {
    val uuidRegex = StringRegex.uuidRegex

    val valid = (1 to 5).map(_ => UUID.randomUUID().toString)

    val invalid = List(
      "000000?0-0000-0000-0000-000000000000",
      "2q33342313-4123-3444-1234-123412341234",
      "12341234-1234-1234-1234-123412341234a",
      "invalid",
      "?????",
      "testest-test-test-test-testtesttest"
    )

    valid.foreach { value =>
      s"accept valid value: $value" in {
        uuidRegex.matches(value) must be(true)
      }
    }

    invalid.foreach { value =>
      s"reject invalid value: $value" in {
        uuidRegex.matches(value) must be(false)
      }
    }
  }
}
