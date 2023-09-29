package net.wiringbits.spra.ui.web.facades.reactadmin

import slinky.core.{BuildingComponent, ExternalComponent}

import scala.scalajs.js
import scala.scalajs.js.|

object TextField extends ExternalComponent {
  case class Props(source: String, label: js.UndefOr[String] = js.undefined)

  def apply(source: String, label: js.UndefOr[String] = js.undefined): BuildingComponent[_, _] =
    super.apply(Props(source, label))

  override val component: String | js.Object = ReactAdmin.TextField
}
