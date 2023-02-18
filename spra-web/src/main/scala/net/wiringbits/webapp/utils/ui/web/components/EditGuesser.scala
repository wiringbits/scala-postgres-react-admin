package net.wiringbits.webapp.utils.ui.web.components

import net.wiringbits.webapp.utils.api.models.AdminGetTables
import net.wiringbits.webapp.utils.ui.web.facades.reactadmin.ReactAdmin.useEditContext
import net.wiringbits.webapp.utils.ui.web.facades.reactadmin._
import net.wiringbits.webapp.utils.ui.web.models.{ButtonAction, ColumnType, DataExplorerSettings}
import net.wiringbits.webapp.utils.ui.web.utils.ResponseGuesser
import org.scalajs.dom
import org.scalajs.macrotaskexecutor.MacrotaskExecutor.Implicits.global
import slinky.core.facade.{Fragment, ReactElement}
import slinky.core.{FunctionalComponent, KeyAddingStage}

import scala.scalajs.js
import scala.util.{Failure, Success}

object EditGuesser {
  case class Props(
      currentTable: AdminGetTables.Response.DatabaseTable,
      dataExplorerSettings: DataExplorerSettings,
      tables: List[AdminGetTables.Response.DatabaseTable]
  )

  def apply(
      currentTable: AdminGetTables.Response.DatabaseTable,
      dataExplorerSettings: DataExplorerSettings,
      tables: List[AdminGetTables.Response.DatabaseTable]
  ): KeyAddingStage = {
    component(Props(currentTable, dataExplorerSettings, tables))
  }

  val component: FunctionalComponent[Props] = FunctionalComponent[Props] { props =>
    def inputs(table: AdminGetTables.Response.DatabaseTable): Seq[ReactElement] =
      ResponseGuesser.getTypesFromResponse(table).map { field =>
        field.`type` match {
          case ColumnType.Date => DateTimeInput(DateTimeInput.Props(source = field.name, disabled = field.disabled))
          case ColumnType.Text => TextInput(TextInput.Props(source = field.name, disabled = field.disabled))
          case ColumnType.Email => TextInput(TextInput.Props(source = field.name, disabled = field.disabled))
          case ColumnType.Image => ImageField(ImageField.Props(source = field.name))
          case ColumnType.Number => NumberInput(NumberInput.Props(source = field.name, disabled = field.disabled))
          case ColumnType.Reference(reference, source) =>
            ReferenceInput(
              ReferenceInput.Props(
                source = field.name,
                reference = reference,
                children = Seq(SelectInput(SelectInput.Props(optionText = source, disabled = field.disabled)))
              )
            )
        }
      }

    def onClick(action: ButtonAction, ctx: js.Dictionary[js.Any]): Unit = {
      val primaryKey = dom.window.location.hash.split("/").lastOption.getOrElse("")
      action.onClick(primaryKey).onComplete {
        case Failure(ex) => ex.printStackTrace()
        case Success(_) => refetch(ctx)
      }
    }

    def refetch(ctx: js.Dictionary[js.Any]): Unit = {
      val _ = ctx.get("refetch").map(_.asInstanceOf[js.Dynamic].apply())
    }

    val tableAction = props.dataExplorerSettings.actions.find(_.tableName == props.currentTable.name)

    def buttons(): Seq[ReactElement] = {
      val ctx = useEditContext()
      tableAction
        .map { x =>
          x.actions.map { action =>
            Button(Button.Props(onClick = () => onClick(action, ctx), children = Seq(action.text)))
          }: Seq[ReactElement]
        }
        .getOrElse(Seq.empty)
    }

    val actions = TopToolbar(TopToolbar.Props(children = buttons()))

    val deleteButton: ReactElement = if (props.currentTable.canBeDeleted) DeleteButton() else Fragment()
    val toolbar: ReactElement = Toolbar(
      Toolbar.Props(children =
        Seq(
          SaveButton(),
          deleteButton
        )
      )
    )

    val joinTables: Seq[ReactElement] = {
      props.currentTable.joinTables.map { case (tableName, columns) =>
        val children: Seq[ReactElement] = {
          val table = props.tables
            .find(_.name == tableName)
            .getOrElse(throw new Exception(s"Table $tableName not found"))

          inputs(table)
        }

        TabbedFormTab(
          TabbedFormTab.Props(
            label = tableName.replace('_', ' '),
            children = Seq(
              ReferenceManyField(
                ReferenceManyField.Props(
                  reference = tableName,
                  target = columns,
                  children = Seq(Datagrid(Datagrid.Props(children = children)))
                )
              )
            )
          )
        )
      }.toSeq
    }

    Edit(
      Edit.Props(
        actions = actions(),
        children = Seq(
          TabbedForm(
            TabbedForm.Props(
              toolbar = toolbar,
              children = Seq(
                TabbedFormTab(
                  TabbedFormTab.Props(
                    label = props.currentTable.name,
                    children = inputs(props.currentTable)
                  )
                ),
                joinTables
              )
            )
          )
        )
      )
    )
  }
}
