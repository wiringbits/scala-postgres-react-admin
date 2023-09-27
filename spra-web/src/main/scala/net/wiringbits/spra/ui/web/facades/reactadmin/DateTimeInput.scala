package net.wiringbits.spra.ui.web.facades.reactadmin

import slinky.core.{BuildingComponent, ExternalComponent}

import scala.scalajs.js
import scala.scalajs.js.|

object DateTimeInput extends ExternalComponent {
  case class Props(
      source: String,
      disabled: Boolean = false,
      isRequired: Boolean = false,
      validate: js.UndefOr[js.Any] = js.undefined
  )

  def apply(
      source: String,
      disabled: Boolean = false,
      isRequired: Boolean = false,
      validate: js.UndefOr[js.Any] = js.undefined
  ): BuildingComponent[_, _] = super.apply(Props(source, disabled, isRequired, validate))

  override val component: String | js.Object = ReactAdmin.DateTimeInput
}
