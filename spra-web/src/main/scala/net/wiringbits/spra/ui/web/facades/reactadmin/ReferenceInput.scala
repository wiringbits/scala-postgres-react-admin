package net.wiringbits.spra.ui.web.facades.reactadmin

import slinky.core.{BuildingComponent, ExternalComponent}

import scala.scalajs.js
import scala.scalajs.js.|

object ReferenceInput extends ExternalComponent {
  case class Props(
      source: String,
      reference: String,
      isRequired: Boolean = false,
      validate: js.UndefOr[js.Any] = js.undefined
  )

  def apply(
      source: String,
      reference: String,
      isRequired: Boolean = false,
      validate: js.UndefOr[js.Any] = js.undefined
  ): BuildingComponent[_, _] = {
    super.apply(Props(source, reference, isRequired, validate))
  }

  override val component: String | js.Object = ReactAdmin.ReferenceInput
}
