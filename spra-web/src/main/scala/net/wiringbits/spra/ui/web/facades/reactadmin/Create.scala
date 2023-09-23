package net.wiringbits.spra.ui.web.facades.reactadmin

import slinky.core.ExternalComponent
import slinky.core.facade.ReactElement

import scala.scalajs.js

object Create extends ExternalComponent {
  case class Props(children: ReactElement*)

  override val component: String | js.Object = ReactAdmin.Create
}
