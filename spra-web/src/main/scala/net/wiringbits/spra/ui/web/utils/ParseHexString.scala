package net.wiringbits.spra.ui.web.utils

import scala.util.{Failure, Success, Try}

object ParseHexString {
  def toByteArray(hexString: String): Array[Byte] = {
    // Check if the argument is a hexadecimal string"
    if (!hexString.startsWith("\\x") || (hexString.length % 2) == 1) {
      throw new IllegalArgumentException(s"Error: Expected a hexadecimal string but found: $hexString")
    }
    // Remove the "\x" prefix from the hex string, as it's not part of the actual data
    val hex = hexString.tail.tail
    Try(hex.grouped(2).map { hex => Integer.parseInt(hex, 16).toByte }.toArray) match {
      case Success(value) => value
      case Failure(_) =>
        throw new IllegalArgumentException(s"Error: Expected a hexadecimal string but found: $hexString")
    }
  }
}
