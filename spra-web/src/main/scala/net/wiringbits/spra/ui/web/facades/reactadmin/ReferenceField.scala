package net.wiringbits.spra.ui.web.facades.reactadmin

import slinky.core.facade.ReactElement
import slinky.core.{BuildingComponent, ExternalComponent}

import scala.scalajs.js
import scala.scalajs.js.|

object ReferenceField extends ExternalComponent {
  case class Props(reference: String, source: String, children: Seq[ReactElement])

  def apply(reference: String, source: String)(children: ReactElement*): BuildingComponent[_, _] = {
    super.apply(Props(reference, source, children))
  }

  override val component: String | js.Object = ReactAdmin.ReferenceField
}
