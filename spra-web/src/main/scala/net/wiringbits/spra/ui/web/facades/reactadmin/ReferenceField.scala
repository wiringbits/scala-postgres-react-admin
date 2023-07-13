package net.wiringbits.spra.ui.web.facades.reactadmin

import slinky.core.ExternalComponent
import slinky.core.facade.ReactElement

import scala.scalajs.js
import scala.scalajs.js.|

object ReferenceField extends ExternalComponent {
  case class Props(reference: String, source: String, children: Seq[ReactElement])
  override val component: String | js.Object = ReactAdmin.ReferenceField
}