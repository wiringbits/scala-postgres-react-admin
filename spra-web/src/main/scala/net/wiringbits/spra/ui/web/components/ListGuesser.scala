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

    def defaultField(reference: String, source: String, children: Seq[ReactElement]): ReactElement =
      ReferenceField(
        ReferenceField.Props(
          reference = reference,
          source = source,
          children = children
        )
      )

    val widgetFields: Seq[ReactElement] = fields.map { field =>
      if !field.isVisible then Fragment()
      else {
        val imageStyles = js.Dynamic.literal("width" -> "100px")
        val styles = js.Dynamic.literal("& img" -> imageStyles)
        field.`type` match {
          case Date => DateField(DateField.Props(source = field.name, showTime = true))
          case Text => TextField(TextField.Props(source = field.name))
          case Email => EmailField(EmailField.Props(source = field.name))
          case Image => ImageField(ImageField.Props(source = field.name, sx = styles))
          case Number => NumberField(NumberField.Props(source = field.name))
          case ColumnType.Reference(reference, source) =>
            defaultField(reference, field.name, Seq(TextField(TextField.Props(source = source))))
        }
      }
    }

    val filterList: Seq[ReactElement] = fields.filter(_.filterable).map { field =>
      field.`type` match {
        case ColumnType.Date => DateInput(DateInput.Props(source = field.name))
        case ColumnType.Text | ColumnType.Email => TextInput(TextInput.Props(source = field.name))
        case ColumnType.Image => Fragment()
        case ColumnType.Number => NumberInput(NumberInput.Props(source = field.name))
        case ColumnType.Reference(reference, source) =>
          defaultField(reference, field.name, Seq(TextField(TextField.Props(source = source))))
      }
    }

    val listToolbar: ReactElement = TopToolbar(
      TopToolbar.Props(
        children = Seq(
          FilterButton(FilterButton.Props(filters = filterList)),
          ExportButton(),
          CreateButton()
        )
      )
    )

    ComponentList(ComponentList.Props(actions = listToolbar, filters = filterList))(
      Datagrid(
        Datagrid.Props(rowClick = "edit", bulkActionButtons = props.response.canBeDeleted, children = widgetFields)
      )
    )
  }
}
