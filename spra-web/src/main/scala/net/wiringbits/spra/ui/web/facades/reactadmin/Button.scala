package net.wiringbits.spra.ui.web.facades.reactadmin

import slinky.core.facade.ReactElement
import slinky.core.{BuildingComponent, ExternalComponent}

import scala.scalajs.js
import scala.scalajs.js.|

object Button extends ExternalComponent {
  case class Props(onClick: () => Unit, children: Seq[ReactElement])

  def apply(onClick: () => Unit)(children: ReactElement*): BuildingComponent[_, _] = {
    super.apply(Props(onClick, children))
  }

  override val component: String | js.Object = ReactAdmin.Button
}
