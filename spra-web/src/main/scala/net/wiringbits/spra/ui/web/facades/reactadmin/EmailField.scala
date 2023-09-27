package net.wiringbits.spra.ui.web.facades.reactadmin

import slinky.core.{BuildingComponent, ExternalComponent}

import scala.scalajs.js
import scala.scalajs.js.|

object EmailField extends ExternalComponent {
  case class Props(source: String)

  def apply(source: String): BuildingComponent[_, _] = {
    super.apply(Props(source))
  }

  override val component: String | js.Object = ReactAdmin.EmailField
}
