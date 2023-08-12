package net.wiringbits.spra.ui.web

import net.wiringbits.spra.api.AdminDataExplorerApiClient
import org.scalajs.macrotaskexecutor.MacrotaskExecutor.Implicits.global
import sttp.capabilities
import sttp.client3.SttpBackend

import scala.concurrent.Future

case class API(client: AdminDataExplorerApiClient, url: String)

object API {
  private val apiUrl = {
    net.wiringbits.BuildInfo.apiUrl.filter(_.nonEmpty).getOrElse {
      "http://localhost:9000"
    }
  }

  def apply(): API = {
    implicit val sttpBackend: SttpBackend[Future, capabilities.WebSockets] = sttp.client3.FetchBackend()
    val admin = new AdminDataExplorerApiClient.DefaultImpl(AdminDataExplorerApiClient.Config(apiUrl))
    API(admin, apiUrl)
  }
}
