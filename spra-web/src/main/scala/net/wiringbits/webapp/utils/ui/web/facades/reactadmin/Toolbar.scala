package net.wiringbits.webapp.utils.ui.web.facades.reactadmin

import slinky.core.ExternalComponent
import slinky.core.facade.ReactElement

import scala.scalajs.js
import scala.scalajs.js.|

object Toolbar extends ExternalComponent {
  case class Props(children: Seq[ReactElement])
  override val component: String | js.Object = ReactAdmin.Toolbar
}
