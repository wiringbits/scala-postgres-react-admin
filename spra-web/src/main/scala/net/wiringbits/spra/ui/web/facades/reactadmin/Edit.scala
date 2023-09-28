package net.wiringbits.spra.ui.web.facades.reactadmin

import slinky.core.facade.ReactElement
import slinky.core.{BuildingComponent, ExternalComponent}

import scala.scalajs.js
import scala.scalajs.js.|

object Edit extends ExternalComponent {
  case class Props(actions: ReactElement)

  def apply(actions: ReactElement): BuildingComponent[_, _] = {
    super.apply(Props(actions))
  }

  override val component: String | js.Object = ReactAdmin.Edit
}
