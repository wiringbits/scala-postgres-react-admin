package net.wiringbits.webapp.utils.ui.web.facades.reactadmin

import slinky.core.ExternalComponent
import slinky.core.facade.ReactElement

import scala.scalajs.js
import scala.scalajs.js.|

object Datagrid extends ExternalComponent {
  case class Props(
      rowClick: js.UndefOr[String] = js.undefined,
      bulkActionButtons: Boolean = false,
      children: Seq[ReactElement]
  )
  override val component: String | js.Object = ReactAdmin.Datagrid
}
