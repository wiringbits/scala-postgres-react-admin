package net.wiringbits.spra.ui.web.components

import net.wiringbits.spra.api.models.AdminGetTables
import net.wiringbits.spra.ui.web.facades.reactadmin.*
import net.wiringbits.spra.ui.web.models.ColumnType
import net.wiringbits.spra.ui.web.models.ColumnType.{Date, Email, Image, Number, Text}
import net.wiringbits.spra.ui.web.utils.ResponseGuesser
import slinky.core.facade.{Fragment, ReactElement}
import slinky.core.{FunctionalComponent, KeyAddingStage}

import scala.scalajs.js

object ListGuesser {
  case class Props(response: AdminGetTables.Response.DatabaseTable)

  def apply(response: AdminGetTables.Response.DatabaseTable): KeyAddingStage = {
    component(Props(response))
  }

  val component: FunctionalComponent[Props] = FunctionalComponent[Props] { props =>
    val fields = ResponseGuesser.getTypesFromResponse(props.response)

    def defaultField(reference: String, source: String)(children: ReactElement*): ReactElement =
      ReferenceField(reference = reference, source = source)(children)

    val widgetFields: Seq[ReactElement] = fields.map { field =>
      if !field.isVisible then Fragment()
      else {
        val imageStyles = js.Dynamic.literal("width" -> "100px")
        val styles = js.Dynamic.literal("& img" -> imageStyles)
        field.`type` match {
          case Date => DateField(source = field.name, showTime = true)
          case Text => TextField(source = field.name)
          case Email => EmailField(source = field.name)
          case Image => ImageField(source = field.name, sx = styles)
          case Number => NumberField(source = field.name)
          case ColumnType.Reference(reference, source) =>
            defaultField(reference, field.name)(
              TextField(source = props.response.referenceDisplayField.getOrElse(source))
            )
        }
      }
    }

    val filterList: Seq[ReactElement] = fields.filter(_.filterable).map { field =>
      field.`type` match {
        case ColumnType.Date => DateInput(source = field.name)
        case ColumnType.Text | ColumnType.Email => TextInput(source = field.name)
        case ColumnType.Image => Fragment()
        case ColumnType.Number => NumberInput(source = field.name)
        case ColumnType.Reference(reference, source) =>
          defaultField(reference, field.name)(TextField(source = source))
      }
    }

    val listToolbar: ReactElement = TopToolbar(
      FilterButton(filters = filterList),
      ExportButton(),
      CreateButton()
    )

    ComponentList(listToolbar)(filterList: _*)(
      Datagrid(rowClick = "edit", bulkActionButtons = props.response.canBeDeleted)(widgetFields)
    )
  }
}
