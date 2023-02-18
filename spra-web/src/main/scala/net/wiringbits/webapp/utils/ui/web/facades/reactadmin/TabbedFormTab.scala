package net.wiringbits.webapp.utils.ui.web.facades.reactadmin

import slinky.core.ExternalComponent
import slinky.core.facade.ReactElement

import scala.scalajs.js
import scala.scalajs.js.|

object TabbedFormTab extends ExternalComponent {
  case class Props(label: String, children: Seq[ReactElement])
  // FormTab is equivalent to TabbedForm.Tab
  override val component: String | js.Object = ReactAdmin.FormTab
}
