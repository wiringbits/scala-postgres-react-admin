package net.wiringbits.spra.ui.web

import net.wiringbits.spra.api.models.AdminGetTables
import net.wiringbits.spra.ui.web.components.{CreateGuesser, EditGuesser, ListGuesser}
import net.wiringbits.spra.ui.web.facades.reactadmin.{Admin, Resource}
import net.wiringbits.spra.ui.web.facades.simpleRestProvider
import net.wiringbits.spra.ui.web.models.DataExplorerSettings
import org.scalajs.macrotaskexecutor.MacrotaskExecutor.Implicits.global
import slinky.core.facade.{Hooks, ReactElement}
import slinky.core.{FunctionalComponent, KeyAddingStage}
import slinky.web.html.{div, h1}

import scala.util.{Failure, Success}

object AdminView {
  case class Props(api: API, dataExplorerSettings: DataExplorerSettings = DataExplorerSettings())

  def apply(api: API, dataExplorerSettings: DataExplorerSettings = DataExplorerSettings()): KeyAddingStage = component(
    Props(api, dataExplorerSettings)
  )

  val component: FunctionalComponent[Props] = FunctionalComponent[Props] { props =>
    val (tables, setTables) = Hooks.useState[List[AdminGetTables.Response.DatabaseTable]](List.empty)
    val (error, setError) = Hooks.useState[Option[String]](Option.empty)

    // We have to find a way to use AsyncComponent instead of useEffect
    Hooks.useEffect(
      () => {
        props.api.client.getTables.onComplete {
          case Success(response) =>
            setTables(response.data)
            setError(None)

          case Failure(ex) =>
            setError(Some(ex.getMessage))
        }
      },
      ""
    )

    val tablesUrl = s"${props.api.url}/admin/tables"

    def buildResources: Seq[ReactElement] = {
      tables.map { table =>
        Resource(
          Resource.Props(
            name = table.name,
            list = ListGuesser(table),
            edit = EditGuesser(table, props.dataExplorerSettings),
            create = CreateGuesser(table)
          )
        ).withKey(table.name)
      }
    }

    div()(
      Admin(Admin.Props(dataProvider = simpleRestProvider(tablesUrl), children = buildResources)),
      error.map(h1(_))
    )
  }
}
