package net.wiringbits.spra.ui.web.facades.reactadmin

import slinky.core.ExternalComponent

import scala.scalajs.js
import scala.scalajs.js.|

object SelectInput extends ExternalComponent {
  case class Props(optionText: String = "", disabled: Boolean = false)
  override val component: String | js.Object = ReactAdmin.SelectInput
}
