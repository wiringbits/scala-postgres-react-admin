package net.wiringbits.spra.ui.web.facades.reactadmin

import slinky.core.ExternalComponent
import slinky.core.facade.ReactElement

import scala.scalajs.js
import scala.scalajs.js.|

object ReferenceInput extends ExternalComponent {
  case class Props(
      source: String,
      reference: String,
      children: Seq[ReactElement],
      isRequired: Boolean = false,
      validate: js.UndefOr[js.Any] = js.undefined
  )
  override val component: String | js.Object = ReactAdmin.ReferenceInput
}
