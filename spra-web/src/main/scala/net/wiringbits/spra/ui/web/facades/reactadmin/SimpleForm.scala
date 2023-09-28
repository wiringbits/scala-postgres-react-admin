package net.wiringbits.spra.ui.web.facades.reactadmin

import slinky.core.facade.ReactElement
import slinky.core.{BuildingComponent, ExternalComponent}

import scala.scalajs.js
import scala.scalajs.js.|

object SimpleForm extends ExternalComponent {
  case class Props(toolbar: ReactElement)

  def apply(toolbar: ReactElement): BuildingComponent[_, _] =
    super.apply(Props(toolbar))

  override val component: String | js.Object = ReactAdmin.SimpleForm
}
