package net.wiringbits.spra.ui.web.facades.reactadmin

import slinky.core.facade.ReactElement
import slinky.core.{BuildingComponent, ExternalComponent}

import scala.scalajs.js
import scala.scalajs.js.|

object Datagrid extends ExternalComponent {
  case class Props(rowClick: String, bulkActionButtons: Boolean, children: Seq[ReactElement])

  def apply(rowClick: String, bulkActionButtons: Boolean)(children: ReactElement*): BuildingComponent[_, _] =
    super.apply(Props(rowClick, bulkActionButtons, children))

  override val component: String | js.Object = ReactAdmin.Datagrid
}
