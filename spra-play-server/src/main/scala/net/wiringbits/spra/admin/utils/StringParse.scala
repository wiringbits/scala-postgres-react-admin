package net.wiringbits.spra.admin.utils

object StringParse {

  def stringToByteArray(value: String): Array[Byte] = {
    try {
      value.replaceAll("[\\[\\]\\s]", "").split(",").map(_.toByte)
    } catch {
      case _: NumberFormatException => Array.emptyByteArray
    }
  }

}
