package net.wiringbits.spra.ui.web.facades.reactadmin

import slinky.core.{BuildingComponent, ExternalComponent}

import scala.scalajs.js
import scala.scalajs.js.|

object ReferenceField extends ExternalComponent {
  case class Props(reference: String, source: String)

  def apply(reference: String, source: String): BuildingComponent[_, _] = {
    super.apply(Props(reference, source))
  }

  override val component: String | js.Object = ReactAdmin.ReferenceField
}
