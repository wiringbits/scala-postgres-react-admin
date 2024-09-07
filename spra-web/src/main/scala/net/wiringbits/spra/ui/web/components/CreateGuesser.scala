package net.wiringbits.spra.ui.web.components

import net.wiringbits.spra.api.models.AdminGetTables
import net.wiringbits.spra.ui.web.facades.reactadmin.*
import net.wiringbits.spra.ui.web.models.ColumnType
import net.wiringbits.spra.ui.web.utils.ResponseGuesser
import slinky.core.facade.{Fragment, ReactElement}
import slinky.core.{FunctionalComponent, KeyAddingStage}

import scala.scalajs.js

object CreateGuesser {
  case class Props(response: AdminGetTables.Response.DatabaseTable)

  def apply(response: AdminGetTables.Response.DatabaseTable): KeyAddingStage = component(Props(response = response))

  val component: FunctionalComponent[Props] = FunctionalComponent[Props] { props =>
    val fields = ResponseGuesser.getTypesFromResponse(props.response)
    val inputs: Seq[ReactElement] = fields.map { field =>
      field.isRequiredOnCreate
        .map { isRequired =>
          val required = if isRequired then ReactAdmin.required() else js.undefined

          field.`type` match {
            case ColumnType.Date =>
              DateTimeInput(source = field.name, isRequired = isRequired, validate = required)
            case ColumnType.Text =>
              TextInput(source = field.name, isRequired = isRequired, validate = required)
            case ColumnType.Email =>
              TextInput(source = field.name, isRequired = isRequired, validate = required)
            case ColumnType.Image =>
              ImageInput(source = field.name, isRequired = isRequired, validate = required)(ImageField(source = "src"))
            case ColumnType.Number =>
              NumberInput(source = field.name, isRequired = isRequired, validate = required)
            case ColumnType.Reference(reference, source) =>
              ReferenceInput(
                source = field.name,
                reference = reference,
                isRequired = isRequired,
                validate = required
              )(
                SelectInput(
                  optionText = props.response.referenceDisplayField.getOrElse(source),
                  isRequired = isRequired,
                  validate = required
                )
              )
          }
        }
        .getOrElse(Fragment())
    }

    Create(
      SimpleForm(Fragment())(inputs.appended(SaveButton()): _*)
    )
  }
}
