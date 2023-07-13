package net.wiringbits.webapp.utils.ui.web

import net.wiringbits.webapp.utils.api.AdminDataExplorerApiClient
import org.scalajs.macrotaskexecutor.MacrotaskExecutor.Implicits.global

case class API(client: AdminDataExplorerApiClient, url: String)

object API {
  private val apiUrl = {
    net.wiringbits.BuildInfo.apiUrl.filter(_.nonEmpty).getOrElse {
      "http://localhost:9000"
    }
  }

  def apply(): API = {
    implicit val sttpBackend = sttp.client3.FetchBackend()
    val admin = new AdminDataExplorerApiClient.DefaultImpl(AdminDataExplorerApiClient.Config(apiUrl))
    API(admin, apiUrl)
  }
}
