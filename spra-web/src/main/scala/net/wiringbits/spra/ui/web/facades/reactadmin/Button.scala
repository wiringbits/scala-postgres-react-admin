package net.wiringbits.spra.ui.web.facades.reactadmin

import slinky.core.{BuildingComponent, ExternalComponent}

import scala.scalajs.js
import scala.scalajs.js.|

object Button extends ExternalComponent {
  case class Props(onClick: () => Unit)

  def apply(onClick: () => Unit): BuildingComponent[_, _] = {
    super.apply(Props(onClick))
  }

  override val component: String | js.Object = ReactAdmin.Button
}
