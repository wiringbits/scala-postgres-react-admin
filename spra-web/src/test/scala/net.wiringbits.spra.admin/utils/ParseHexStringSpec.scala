package net.wiringbits.spra.admin

import net.wiringbits.spra.ui.web.utils.ParseHexString
import org.scalatest.matchers.must.Matchers.{be, must}
import org.scalatest.wordspec.AnyWordSpec
import scala.util.Try

class ParseHexStringSpec extends AnyWordSpec {

  "convert valids hex string to a byte array" should {
    val hexData = List(
      "\\x5F3A9C1B7D",
      "\\x9E2D5B8F2A",
      "\\xA3B7D2E6C4",
      "\\x4F1E9A6D3B",
      "\\x6C2A8F4B7E",
      "\\xA3B9F56E8D4C721F9A6D3F2C",
      "\\x5F2C8E7B9A1D4E6F3B7A4F2D",
      "\\x7E9B6C2A5F8D4B3C6A2E1F9D",
      "\\xD4A7C8F25B3E7A9F4C8D6E1B",
      "\\xF3A59B4C7D2E8F1A6B9D3C4F2A7E1D5C9A3B6E8D4F2C1A7B3D9E4F1C6A2B"
    )

    val conversionBytea: List[Array[Byte]] = List(
      Array(0x5f, 0x3a, 0x9c, 0x1b, 0x7d),
      Array(0x9e, 0x2d, 0x5b, 0x8f, 0x2a),
      Array(0xa3, 0xb7, 0xd2, 0xe6, 0xc4),
      Array(0x4f, 0x1e, 0x9a, 0x6d, 0x3b),
      Array(0x6c, 0x2a, 0x8f, 0x4b, 0x7e),
      Array(0xa3, 0xb9, 0xf5, 0x6e, 0x8d, 0x4c, 0x72, 0x1f, 0x9a, 0x6d, 0x3f, 0x2c),
      Array(0x5f, 0x2c, 0x8e, 0x7b, 0x9a, 0x1d, 0x4e, 0x6f, 0x3b, 0x7a, 0x4f, 0x2d),
      Array(0x7e, 0x9b, 0x6c, 0x2a, 0x5f, 0x8d, 0x4b, 0x3c, 0x6a, 0x2e, 0x1f, 0x9d),
      Array(0xd4, 0xa7, 0xc8, 0xf2, 0x5b, 0x3e, 0x7a, 0x9f, 0x4c, 0x8d, 0x6e, 0x1b),
      Array(0xf3, 0xa5, 0x9b, 0x4c, 0x7d, 0x2e, 0x8f, 0x1a, 0x6b, 0x9d, 0x3c, 0x4f, 0x2a, 0x7e, 0x1d, 0x5c, 0x9a, 0x3b,
        0x6e, 0x8d, 0x4f, 0x2c, 0x1a, 0x7b, 0x3d, 0x9e, 0x4f, 0x1c, 0x6a, 0x2b)
    ).map(_.map(_.toByte))

    hexData.zip(conversionBytea).foreach { case (hex, expectedBytes) =>
      s"convert valid data $hex" in {
        ParseHexString.toByteArray(hex) must be(expectedBytes)
      }
    }
  }

  "throw an exception for a string containing non-hexadecimal characters" should {
    val hexData = List(
      "\\a5F3A9C1B7D",
      "9E2D5B8F2A",
      "\\xG3B7D2E6C4",
      "\\x4F1E9A76D3B",
      "6C42A8F4G7E",
      "\\xA3B9F56E8P4C721F9A6D3F2C",
      "\\x5F2C8E7B9AA1D4E6F3B7A4F2D",
      "\\x7E96C2A5F8D4B3C6A2E1F9D",
      "\\xD4A7C8F25B3E7JLAKSNSLKAS",
      "\\T1542ABF3A59B4C7D2E8F1A6B9D3C4F2A7E1D5C9A3B6E8D4F2C1A7B3D9E4F1C6A2B"
    )

    hexData.foreach { value =>
      s"throw an exeption for: $value" in {
        Try(ParseHexString.toByteArray(value)).isFailure must be(true)
      }
    }
  }
}
