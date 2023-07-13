package net.wiringbits.spra.ui.web.facades.reactadmin

import slinky.core.ExternalComponent
import slinky.core.facade.ReactElement

import scala.scalajs.js
import scala.scalajs.js.|

object SimpleForm extends ExternalComponent {
  case class Props(toolbar: ReactElement, children: Seq[ReactElement])
  override val component: String | js.Object = ReactAdmin.SimpleForm
}
