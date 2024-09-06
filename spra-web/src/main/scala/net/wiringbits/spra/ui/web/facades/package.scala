package net.wiringbits.spra.ui.web

import scala.scalajs.js
import net.wiringbits.spra.ui.web.utils.Images.*
import org.scalajs.dom.File

package object facades {

  def createDataProvider(url: String): DataProvider = {
    val baseDataProvider = simpleRestProvider(url)
    WithLifecycleCallbacks(
      baseDataProvider,
      js.Array(
        js.Dynamic.literal(
          resource = "images",
          afterRead = (record: js.Dynamic, dataProvider: js.Any) => {
            val hexImage = record.data.asInstanceOf[String]
            val urlImage = convertHexToImage(hexImage)
            record.updateDynamic("data")(urlImage)
            record
          },
          beforeSave = (data: js.Dynamic, dataProvider: js.Any) => {
            val rawFile = data.data.rawFile.asInstanceOf[File]
            convertImageToByteArray(rawFile).`then` { value =>
              data.updateDynamic("data")(value.asInstanceOf[js.Any])
              data
            }
          }
        )
      )
    )
  }
}
