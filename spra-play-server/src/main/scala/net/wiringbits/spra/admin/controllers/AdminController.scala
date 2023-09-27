package net.wiringbits.spra.admin.controllers

import net.wiringbits.spra.admin.config.DataExplorerConfig
import net.wiringbits.spra.admin.services.AdminService
import net.wiringbits.spra.admin.utils.models.QueryParameters
import net.wiringbits.spra.api.models
import net.wiringbits.spra.api.models.*
import org.slf4j.LoggerFactory
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}

import javax.inject.Inject
import scala.concurrent.ExecutionContext

// TODO: Remove authentication, which should be provided by each app
class AdminController @Inject() (
    adminService: AdminService,
    dataExplorerConfig: DataExplorerConfig
)(implicit cc: ControllerComponents, ec: ExecutionContext)
    extends AbstractController(cc) {
  private val logger = LoggerFactory.getLogger(this.getClass)

  def getTables() = handleGET { request =>
    for {
      _ <- adminUser(request)
      _ = logger.info(s"Get database tables")
      response <- adminService.tables()
    } yield Ok(Json.toJson(response))
  }

  def getTableMetadata(tableName: String, queryParams: QueryParameters) = handleGET { request =>
    for {
      _ <- adminUser(request)
      _ = logger.info(s"Get metadata for $tableName, parameters: $queryParams")
      (response, contentRange) <- adminService.tableMetadata(tableName, queryParams)
      // Json.toJson doesn't support a List[Map[_, _]] so we need to map to convert it to List[JsonValue]
    } yield Ok(Json.toJson(response.map(Json.toJson(_))))
      .withHeaders(("Access-Control-Expose-Headers", "Content-Range"), ("Content-Range", contentRange))
  }

  def find(tableName: String, primaryKeyValue: String) = handleGET { request =>
    for {
      _ <- adminUser(request)
      _ = logger.info(s"Get data from $tableName where primaryKey = $primaryKeyValue")
      response <- adminService.find(tableName, primaryKeyValue)
    } yield Ok(Json.toJson(response))
  }

  def find(tableName: String, primaryKeyValues: List[String]) = handleGET { request =>
    for {
      _ <- adminUser(request)
      _ = logger.info(s"Get data from $tableName where primaryKeys = ${primaryKeyValues.mkString(",")}")
      response <- adminService.find(tableName, primaryKeyValues)
    } yield Ok(Json.toJson(response.map(Json.toJson(_))))
  }

  def create(tableName: String) = handleJsonBody[Map[String, String]] { request =>
    val body = AdminCreateTable.Request(request.body)
    for {
      _ <- adminUser(request)
      _ = logger.info(s"Create row in $tableName: ${body.data}")
      id <- adminService.create(tableName, body)
    } yield Ok(Json.toJson(Map("id" -> id)))
  }

  def update(tableName: String, primaryKeyValue: String) = handleJsonBody[AdminUpdateTable.Request] { request =>
    val primaryKeyFieldName = dataExplorerConfig.unsafeFindByName(tableName).primaryKeyField
    val body = request.body.data.map {
      case ("id", value) => primaryKeyFieldName -> value
      case x => x
    }
    for {
      _ <- adminUser(request)
      _ = logger.info(s"Update row from $tableName where primaryKey = $primaryKeyValue, body = $body")
      _ <- adminService.update(tableName, primaryKeyValue, body)
      response = AdminUpdateTable.Response(id = primaryKeyValue)
    } yield Ok(Json.toJson(response))
  }

  def delete(tableName: String, id: String) = handleGET { request =>
    for {
      _ <- adminUser(request)
      _ = logger.info(s"Delete row from $tableName, id = $id")
      _ <- adminService.delete(tableName, id)
      response = AdminDeleteTable.Response()
    } yield Ok(Json.toJson(response))
  }
}
