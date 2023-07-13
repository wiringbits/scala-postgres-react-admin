package net.wiringbits.spra.ui.web.facades.reactadmin

import slinky.core.ExternalComponent

import scala.scalajs.js
import scala.scalajs.js.|

object NumberField extends ExternalComponent {
  case class Props(source: String)
  override val component: String | js.Object = ReactAdmin.NumberField
}