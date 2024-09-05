package net.wiringbits.spra.admin

import net.wiringbits.spra.admin.utils.StringParse.stringToByteArray
import org.scalatest.matchers.must.Matchers.{be, must}
import org.scalatest.wordspec.AnyWordSpec

class StringParseSpec extends AnyWordSpec {
  "dataParse" should {
    val data = List(
      ("[0, 1, 2, 3, 4, 5, 6, 7, 8, 9]", Array[Byte](0, 1, 2, 3, 4, 5, 6, 7, 8, 9)),
      ("[-128, -64, 0, 64, 127]", Array[Byte](-128, -64, 0, 64, 127)),
      ("[10, 20, 30, 40, 50]", Array[Byte](10, 20, 30, 40, 50)),
      ("[127, -127, 127, -127]", Array[Byte](127, -127, 127, -127))
    )

    data.foreach { (data, valid) =>
      s"accept valid conversion: $data" in {
        stringToByteArray(data) must be(valid)
      }
    }
  }
}
