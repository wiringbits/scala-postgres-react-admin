package net.wiringbits.spra.ui.web.utils

import org.scalajs.dom
import org.scalajs.dom.{Blob, File}
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
    val imageBinary: Array[Byte] = ParseHexString.toByteArray(imageHex)
    val byteArray = Uint8Array(js.Array(imageBinary.map(_.toShort): _*))
    dom.URL.createObjectURL(dom.Blob(js.Array(byteArray.buffer)))
  }
}
