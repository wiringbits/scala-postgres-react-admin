package net.wiringbits.spra.admin.utils

import scala.util.{Failure, Success, Try}

object StringParse {

  def stringToByteArray(value: String): Array[Byte] = {
    // Removes whitespace characters (\\s) and brackets ([, ]) to prepare the string for byte array conversion
    Try(value.replaceAll("[\\[\\]\\s]", "").split(",").map(_.toByte)) match
      case Success(value) => value
      case Failure(_) => Array.emptyByteArray
  }

}
