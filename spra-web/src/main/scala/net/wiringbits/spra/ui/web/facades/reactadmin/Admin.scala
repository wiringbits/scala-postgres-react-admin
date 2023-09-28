package net.wiringbits.spra.ui.web.facades.reactadmin

import net.wiringbits.spra.ui.web.facades.DataProvider
import slinky.core.{BuildingComponent, ExternalComponent}

import scala.scalajs.js
import scala.scalajs.js.|

object Admin extends ExternalComponent {
  case class Props(dataProvider: DataProvider)

  def apply(dataProvider: DataProvider): BuildingComponent[_, _] = {
    super.apply(Props(dataProvider))
  }

  override val component: String | js.Object = ReactAdmin.Admin
}
