package net.wiringbits.spra.ui.web.facades.reactadmin

import slinky.core.ExternalComponent
import slinky.core.facade.ReactElement

import scala.scalajs.js
import scala.scalajs.js.|

object Datagrid extends ExternalComponent {
  case class Props(rowClick: String, bulkActionButtons: Boolean, children: Seq[ReactElement])
  override val component: String | js.Object = ReactAdmin.Datagrid
}
