package net.wiringbits.spra.ui.web.facades.reactadmin

import slinky.core.facade.ReactElement
import slinky.core.{BuildingComponent, ExternalComponent}

import scala.scalajs.js
import scala.scalajs.js.|

object TopToolbar extends ExternalComponent {
  case class Props(children: Seq[ReactElement])

  def apply(children: ReactElement*): BuildingComponent[_, _] = {
    super.apply(Props(children))
  }

  override val component: String | js.Object = ReactAdmin.TopToolbar
}
