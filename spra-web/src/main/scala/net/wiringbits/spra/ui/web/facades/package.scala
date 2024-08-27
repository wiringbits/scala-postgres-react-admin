package net.wiringbits.spra.ui.web

import scala.scalajs.js

package object facades {

  def createDataProvider(url: String): DataProvider = {

    val baseDataProvider = simpleRestProvider(url)

    val dataProvider = WithLifecycleCallbacks(
      baseDataProvider,
      js.Array(
        js.Dynamic.literal(
          resource = "images",
          afterRead = (record: js.Dynamic, dataProvider: js.Any) => {
            processResponse(record)
          },
          beforeSave = (data: js.Dynamic, dataProvider: js.Any) => {
            prepareRequest(data)
          }
        )
      )
    )

    dataProvider
  }
}
