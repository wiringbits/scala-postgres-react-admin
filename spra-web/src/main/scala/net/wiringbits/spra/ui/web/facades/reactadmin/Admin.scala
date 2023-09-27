package net.wiringbits.spra.ui.web.facades.reactadmin

import net.wiringbits.spra.ui.web.facades.DataProvider
import slinky.core.facade.ReactElement
import slinky.core.{BuildingComponent, ExternalComponent}

import scala.scalajs.js
import scala.scalajs.js.|

object Admin extends ExternalComponent {
  case class Props(dataProvider: DataProvider, children: Seq[ReactElement])

  def apply(dataProvider: DataProvider)(children: ReactElement*): BuildingComponent[_, _] = {
    super.apply(Props(dataProvider, children))
  }

  override val component: String | js.Object = ReactAdmin.Admin
}
