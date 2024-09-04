package net.wiringbits.spra.ui.web.utils

import org.scalajs.dom
import org.scalajs.dom.{Blob, File}
import scala.util.{Failure, Success, Try}
import scala.scalajs.js.Promise
import scala.scalajs.js.typedarray.{ArrayBuffer, Int8Array, Uint8Array}
import scala.scalajs.js

object Images {
  def convertImageToByteArray(image: dom.File): js.Promise[String] = {
    new js.Promise[String]((resolve, reject) => {
      val reader = new dom.FileReader()
      reader.onload = { (e: dom.Event) =>
        val arrayBuffer = reader.result.asInstanceOf[ArrayBuffer]
        val byteArray = new Int8Array(arrayBuffer).toArray
        resolve(byteArray.mkString("[", ", ", "]"))
      }
      reader.onerror = { (e: dom.Event) =>
        reject(new js.Error("Failed to read file"))
      }
      reader.readAsArrayBuffer(image)
    })
  }

  def convertHexToImage(imageHex: String): String = {
    // Remove the "0x" prefix from the hex string, as it's not part of the actual image data
    val hex = imageHex.tail.tail
    val imageBinary: Array[Byte] =
      if ((hex.length % 2) == 1)
        Array.empty
      else
        Try(hex.grouped(2).map { hex => Integer.parseInt(hex, 16).toByte }.toArray) match {
          case Success(value) => value
          case Failure(_) => Array.empty
        }
    val byteArray = Uint8Array(js.Array(imageBinary.map(_.toShort): _*))
    dom.URL.createObjectURL(dom.Blob(js.Array(byteArray.buffer)))
  }
}
