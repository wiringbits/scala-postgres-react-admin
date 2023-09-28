package net.wiringbits.spra.ui.web.facades.reactadmin

import slinky.core.{BuildingComponent, ExternalComponent}

import scala.scalajs.js
import scala.scalajs.js.|

object Datagrid extends ExternalComponent {
  case class Props(rowClick: String, bulkActionButtons: Boolean)

  def apply(rowClick: String, bulkActionButtons: Boolean): BuildingComponent[_, _] =
    super.apply(Props(rowClick, bulkActionButtons))

  override val component: String | js.Object = ReactAdmin.Datagrid
}
