package net.wiringbits.spra.api

import net.wiringbits.spra.api.models.*
import play.api.libs.json.*
import sttp.client3.*
import sttp.model.*

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

trait AdminDataExplorerApiClient {
  def getTables: Future[AdminGetTables.Response]

  def getTableMetadata(
      tableName: String,
      sort: List[String],
      range: List[Int],
      filters: String
  ): Future[List[Map[String, String]]]

  def viewItem(tableName: String, id: String): Future[Map[String, String]]

  def viewItems(tableName: String, ids: List[String]): Future[List[Map[String, String]]]

  def createItem(tableName: String, request: AdminCreateTable.Request): Future[AdminCreateTable.Response]

  def updateItem(tableName: String, id: String, request: AdminUpdateTable.Request): Future[AdminUpdateTable.Response]

  def deleteItem(tableName: String, id: String): Future[AdminDeleteTable.Response]
}

object AdminDataExplorerApiClient {
  case class Config(serverUrl: String)

  private def asJson[R: Reads] = {
    asString
      .map {
        case Right(response) =>
          // handles 2xx responses
          Success(response)
        case Left(response) =>
          // handles non 2xx responses
          Try {
            val json = Json.parse(response)
            // TODO: Unify responses to match the play error format
            json
              .asOpt[ErrorResponse]
              .orElse {
                json
                  .asOpt[PlayErrorResponse]
                  .map(model => ErrorResponse(model.error.message))
              }
              .getOrElse(throw new RuntimeException(s"Unexpected JSON response: $response"))
          } match {
            case Failure(exception) =>
              println(s"Unexpected response: ${exception.getMessage}")
              exception.printStackTrace()
              Failure(new RuntimeException(s"Unexpected response, please try again in a minute"))
            case Success(value) =>
              Failure(new RuntimeException(value.error))
          }
      }
      .map { t =>
        t.map(Json.parse).map(_.as[R])
      }
  }

  class DefaultImpl(config: Config)(implicit
      backend: SttpBackend[Future, _],
      ec: ExecutionContext
  ) extends AdminDataExplorerApiClient {

    private val ServerAPI = sttp.model.Uri
      .parse(config.serverUrl)
      .getOrElse(throw new RuntimeException("Invalid server url"))

    private def prepareRequest[R: Reads] = {
      basicRequest
        .contentType(MediaType.ApplicationJson)
        .response(asJson[R])
    }

    override def getTables: Future[AdminGetTables.Response] = {
      val path = ServerAPI.path :+ "admin" :+ "tables"
      val uri = ServerAPI.withPath(path)

      prepareRequest[AdminGetTables.Response]
        .get(uri)
        .send(backend)
        .map(_.body)
        .flatMap(Future.fromTry)
    }

    override def getTableMetadata(
        tableName: String,
        sort: List[String],
        range: List[Int],
        filters: String
    ): Future[List[Map[String, String]]] = {
      val path = ServerAPI.path :+ "admin" :+ "tables" :+ tableName
      val parameters: Map[String, String] = Map(
        "sort" -> sort.mkString("[", ",", "]"),
        "range" -> range.mkString("[", ",", "]"),
        "filter" -> filters
      )
      val uri = ServerAPI
        .withPath(path)
        .addParams(parameters)

      prepareRequest[List[Map[String, String]]]
        .get(uri)
        .send(backend)
        .map(_.body)
        .flatMap(Future.fromTry)
    }

    override def viewItem(tableName: String, id: String): Future[Map[String, String]] = {
      val path = ServerAPI.path :+ "admin" :+ "tables" :+ tableName :+ id
      val uri = ServerAPI.withPath(path)

      prepareRequest[Map[String, String]]
        .get(uri)
        .send(backend)
        .map(_.body)
        .flatMap(Future.fromTry)
    }

    override def viewItems(tableName: String, id: List[String]): Future[List[Map[String, String]]] = {
      val path = ServerAPI.path :+ "admin" :+ "tables" :+ tableName
      val primaryKeyParam = Json.toJson(Map("id" -> id)).toString()
      val uri = ServerAPI.withPath(path).withParams(Map("filter" -> primaryKeyParam))
      prepareRequest[List[Map[String, String]]]
        .get(uri)
        .send(backend)
        .map(_.body)
        .flatMap(Future.fromTry)
    }

    override def createItem(tableName: String, request: AdminCreateTable.Request): Future[AdminCreateTable.Response] = {
      val path = ServerAPI.path :+ "admin" :+ "tables" :+ tableName
      val uri = ServerAPI.withPath(path)

      prepareRequest[AdminCreateTable.Response]
        .post(uri)
        // TODO: check the AdminCreateTable.Request model, it isn't parsing correctly to json
        .body(Json.toJson(request.data).toString())
        .send(backend)
        .map(_.body)
        .flatMap(Future.fromTry)
    }

    override def updateItem(
        tableName: String,
        id: String,
        request: AdminUpdateTable.Request
    ): Future[AdminUpdateTable.Response] = {
      val path = ServerAPI.path :+ "admin" :+ "tables" :+ tableName :+ id
      val uri = ServerAPI.withPath(path)

      prepareRequest[AdminUpdateTable.Response]
        .put(uri)
        .body(Json.toJson(request).toString())
        .send(backend)
        .map(_.body)
        .flatMap(Future.fromTry)
    }

    override def deleteItem(tableName: String, id: String): Future[AdminDeleteTable.Response] = {
      val path = ServerAPI.path :+ "admin" :+ "tables" :+ tableName :+ id
      val uri = ServerAPI.withPath(path)

      prepareRequest[AdminDeleteTable.Response]
        .delete(uri)
        .send(backend)
        .map(_.body)
        .flatMap(Future.fromTry)
    }
  }
}
