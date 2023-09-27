package net.wiringbits.spra.ui.web.facades.reactadmin

import slinky.core.facade.ReactElement
import slinky.core.{BuildingComponent, ExternalComponent}

import scala.scalajs.js
import scala.scalajs.js.|

object FilterButton extends ExternalComponent {
  case class Props(filters: Seq[ReactElement])

  def apply(filters: ReactElement*): BuildingComponent[_, _] = {
    super.apply(Props(filters))
  }

  override val component: String | js.Object = ReactAdmin.FilterButton
}
