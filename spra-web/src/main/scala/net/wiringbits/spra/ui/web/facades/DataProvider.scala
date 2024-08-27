package net.wiringbits.spra.ui.web.facades

import org.scalajs.dom
import org.scalajs.dom.{Blob, File}

import scala.concurrent.Future
import scala.scalajs.js
import scala.scalajs.js.{JSON, Promise}
import scala.scalajs.js.annotation.{JSExportTopLevel, JSImport}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js.typedarray.{ArrayBuffer, Int8Array, Uint8Array}

@js.native
trait DataProvider extends js.Object

@js.native
@JSImport("ra-data-simple-rest", JSImport.Default)
def simpleRestProvider(url: String): DataProvider = js.native
@js.native
@JSImport("react-admin", "withLifecycleCallbacks")
object WithLifecycleCallbacks extends js.Object {
  def apply(dataProvider: DataProvider, callbacks: js.Array[js.Object]): DataProvider = js.native
}

def prepareRequest(params: js.Dynamic) = {
  val rawFile = params.data.rawFile.asInstanceOf[File]

  val imageFuture = convertImageToByteArray(rawFile)

  imageFuture.`then` { value =>
    val newParams = params
    params.updateDynamic("data")(value.asInstanceOf[js.Any])
    params
  }
}

def processResponse(record: js.Dynamic) = {
  val hexImage = record.data.asInstanceOf[String]
  val urlImage = convertHexToImage(hexImage)
  record.updateDynamic("data")(urlImage)
  record
}

def convertImageToByteArray(file: dom.File): js.Promise[String] = {
  val promise = new js.Promise[String]((resolve, reject) => {
    val reader = new dom.FileReader()
    reader.onload = { (e: dom.Event) =>
      val arrayBuffer = reader.result.asInstanceOf[ArrayBuffer]
      val byteArray = new Int8Array(arrayBuffer).toArray
      resolve(byteArray.mkString("[", ", ", "]"))
    }
    reader.onerror = { (e: dom.Event) =>
      reject(new js.Error("Failed to read file"))
    }
    reader.readAsArrayBuffer(file)
  })

  promise
}
def convertHexToImage(imageHex: String): String = {

  val hex = imageHex.tail.tail

  val imageBinary: Array[Byte] =
    if ((hex.length % 2) == 1)
      Array.empty
    else
      try {
        val binary = hex
          .grouped(2)
          .map { hex =>
            Integer.parseInt(hex, 16).toByte
          }
          .toArray
        binary
      } catch case _ => Array.empty

  val byteArray = Uint8Array(js.Array(imageBinary.map(_.toShort): _*))

  dom.URL.createObjectURL(dom.Blob(js.Array(byteArray.buffer)))
}
