package net.wiringbits.spra.ui.web.facades.reactadmin

import slinky.core.ExternalComponent

import scala.scalajs.js
import scala.scalajs.js.|

object NumberInput extends ExternalComponent {
  case class Props(
      source: String,
      disabled: Boolean = false,
      isRequired: Boolean = false,
      validate: js.UndefOr[js.Any] = js.undefined
  )
  override val component: String | js.Object = ReactAdmin.NumberInput
}
