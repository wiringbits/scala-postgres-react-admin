package net.wiringbits.spra.ui.web.facades.reactadmin

import slinky.core.ExternalComponent

import scala.scalajs.js
import scala.scalajs.js.|

object DateInput extends ExternalComponent {
  case class Props(source: String, isRequired: Boolean = false, validate: js.UndefOr[js.Any] = js.undefined)


  override val component: String | js.Object = ReactAdmin.DateInput
}
