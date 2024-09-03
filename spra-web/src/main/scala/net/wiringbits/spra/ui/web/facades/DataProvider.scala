package net.wiringbits.spra.ui.web.facades

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@js.native
trait DataProvider extends js.Object

@js.native
@JSImport("ra-data-simple-rest", JSImport.Default)
// https://www.npmjs.com/package/ra-data-simple-rest
def simpleRestProvider(url: String): DataProvider = js.native

@js.native
@JSImport("react-admin", "withLifecycleCallbacks")
// https://marmelab.com/react-admin/withLifecycleCallbacks.html
object WithLifecycleCallbacks extends js.Object {
  def apply(dataProvider: DataProvider, callbacks: js.Array[js.Object]): DataProvider = js.native
}
