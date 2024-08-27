package net.wiringbits.spra.ui.web.facades.reactadmin

import slinky.core.{BuildingComponent, ExternalComponent}

import scala.scalajs.js
import scala.scalajs.js.|

object ImageInput extends ExternalComponent {
  case class Props(
      source: String,
      disabled: Boolean = false,
      sx: js.Dynamic = js.Dynamic.literal(),
      isRequired: Boolean = false,
      validate: js.UndefOr[js.Any] = js.undefined
  )

  def apply(
      source: String,
      disabled: Boolean = false,
      sx: js.Dynamic = js.Dynamic.literal(),
      isRequired: Boolean = false,
      validate: js.UndefOr[js.Any] = js.undefined,
      onDrop: js.UndefOr[js.Function2[js.Array[js.Any], js.Function1[js.Array[js.Object], Unit], Unit]] = js.undefined
  ): BuildingComponent[_, _] = {
    super.apply(Props(source, disabled, sx, isRequired, validate))
  }

  override val component: String | js.Object = ReactAdmin.ImageInput
}
