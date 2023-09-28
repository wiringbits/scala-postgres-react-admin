package net.wiringbits.spra.ui.web.facades.reactadmin

import slinky.core.{BuildingComponent, ExternalComponent}

import scala.scalajs.js
import scala.scalajs.js.|

object Resource extends ExternalComponent {
  case class Props(name: String, edit: js.Object, list: js.Object, create: js.Object)

  def apply(name: String, edit: js.Object, list: js.Object, create: js.Object): BuildingComponent[_, _] = {
    this.apply(Props(name, edit, list, create))
  }

  override val component: String | js.Object = ReactAdmin.Resource
}
