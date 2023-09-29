package net.wiringbits.spra.ui.web.facades.reactadmin

import slinky.core.{BuildingComponent, ExternalComponent}

import scala.scalajs.js

object ReferenceManyField extends ExternalComponent {
  case class Props(target: String, reference: String)

  def apply(target: String, reference: String): BuildingComponent[_, _] =
    this.apply(Props(target, reference))

  override val component: String | js.Object = ReactAdmin.ReferenceManyField
}
