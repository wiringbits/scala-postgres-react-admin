package net.wiringbits.spra.ui.web.facades.reactadmin

import slinky.core.{BuildingComponent, ExternalComponent}

import scala.scalajs.js
import scala.scalajs.js.|

object ImageField extends ExternalComponent {
  case class Props(
      source: String,
      title: String,
      sortable: Boolean = false,
      disabled: Boolean = false,
      sx: js.Dynamic = js.Dynamic.literal()
  )

  def apply(
      source: String,
      title: String = "title",
      sortable: Boolean = false,
      disabled: Boolean = false,
      sx: js.Dynamic = js.Dynamic.literal()
  ): BuildingComponent[_, _] = {
    super.apply(Props(source, title, sortable, disabled, sx))
  }

  override val component: String | js.Object = ReactAdmin.ImageField
}
