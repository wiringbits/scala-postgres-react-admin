package net.wiringbits.spra.ui.web.facades.reactadmin

import slinky.core.{BuildingComponent, ExternalComponent}

import scala.scalajs.js
import scala.scalajs.js.|

object DateField extends ExternalComponent {
  case class Props(source: String, showTime: Boolean)

  def apply(source: String, showTime: Boolean = false): BuildingComponent[_, _] = {
    super.apply(Props(source, showTime))
  }

  override val component: String | js.Object = ReactAdmin.DateField
}
