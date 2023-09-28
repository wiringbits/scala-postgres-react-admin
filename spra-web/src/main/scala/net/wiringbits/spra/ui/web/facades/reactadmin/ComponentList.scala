package net.wiringbits.spra.ui.web.facades.reactadmin

import slinky.core.facade.ReactElement
import slinky.core.{BuildingComponent, ExternalComponent}

import scala.scalajs.js
import scala.scalajs.js.|

object ComponentList extends ExternalComponent {
  case class Props(actions: ReactElement, filters: Seq[ReactElement])

  def apply(actions: ReactElement)(filters: ReactElement*): BuildingComponent[_, _] = {
    super.apply(Props(actions, filters))
  }

  override val component: String | js.Object = ReactAdmin.List
}
