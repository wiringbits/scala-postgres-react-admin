package net.wiringbits.spra.ui.web

import org.scalajs.dom
import slinky.core.facade.ReactElement
import slinky.web.ReactDOM

object Main {
  private def App(): ReactElement = {
    val api = API()

    AdminView(api)
  }

  def main(args: Array[String]): Unit = {
    val app = App()
    ReactDOM.render(app, dom.document.getElementById("root"))
    ()
  }
}
