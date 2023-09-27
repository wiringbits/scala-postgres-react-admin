package net.wiringbits.spra.admin.repositories

import net.wiringbits.spra.admin.config.{DataExplorerConfig, TableSettings}
import net.wiringbits.spra.admin.executors.DatabaseExecutionContext
import net.wiringbits.spra.admin.repositories.daos.DatabaseTablesDAO
import net.wiringbits.spra.admin.repositories.models.{DatabaseTable, ForeignKey, TableColumn, TableData}
import net.wiringbits.spra.admin.utils.models.QueryParameters
import play.api.db.Database

import javax.inject.Inject
import scala.concurrent.Future

class DatabaseTablesRepository @Inject() (database: Database)(implicit
    ec: DatabaseExecutionContext,
    dataExplorerConfig: DataExplorerConfig
) {
  def all(): Future[List[DatabaseTable]] = Future {
    database.withConnection { implicit conn =>
      DatabaseTablesDAO.all()
    }
  }

  def getTableColumns(tableName: String): Future[List[TableColumn]] = Future {
    database.withConnection { implicit conn =>
      DatabaseTablesDAO.getTableColumns(tableName)
    }
  }

  def getForeignKeys(tableName: String): Future[List[ForeignKey]] = Future {
    database.withConnection { implicit conn =>
      DatabaseTablesDAO.getForeignKeys(tableName)
    }
  }

  def getMandatoryFields(tableName: String): Future[List[TableColumn]] = Future {
    database.withConnection { implicit conn =>
      val primaryKeyField = dataExplorerConfig.unsafeFindByName(tableName).primaryKeyField
      DatabaseTablesDAO.getMandatoryFields(tableName, primaryKeyField)
    }
  }

  def getTableMetadata(settings: TableSettings, queryParameters: QueryParameters): Future[List[TableData]] = Future {
    database.withTransaction { implicit conn =>
      val columns = DatabaseTablesDAO.getTableColumns(settings.tableName)
      val rows = DatabaseTablesDAO.getTableData(settings, columns, queryParameters, dataExplorerConfig.baseUrl)
      val columnNames = getColumnNames(columns, settings.primaryKeyField)
      rows.map { row =>
        val tableRow = row.convertToMap(columnNames)
        TableData(tableRow)
      }
    }
  }

  private def getColumnNames(columns: List[TableColumn], primaryKeyField: String) = {
    val columnNames = columns.map(_.name)
    // react-admin looks for an "id" field instead of "user_id", "user_log_id", etc..
    columnNames.updated(columnNames.indexOf(primaryKeyField), "id")
  }

  def find(tableName: String, primaryKeyValue: String): Future[Option[TableData]] = Future {
    database.withTransaction { implicit conn =>
      val settings = dataExplorerConfig.unsafeFindByName(tableName)
      val columns = DatabaseTablesDAO.getTableColumns(tableName)
      val maybe = DatabaseTablesDAO.find(settings, columns, primaryKeyValue, dataExplorerConfig.baseUrl)
      val columnNames = getColumnNames(columns, settings.primaryKeyField)
      maybe.map(x => TableData(x.convertToMap(columnNames)))
    }
  }

  def create(tableName: String, body: Map[String, String]): Future[String] = Future {
    database.withConnection { implicit conn =>
      val primaryKeyField = dataExplorerConfig.unsafeFindByName(tableName).primaryKeyField
      val primaryKeyType = dataExplorerConfig.unsafeFindByName(tableName).primaryKeyDataType
      val columns = DatabaseTablesDAO.getTableColumns(tableName)
      val fieldsAndValues = body.map { case (key, value) =>
        val field =
          columns.find(_.name == key).getOrElse(throw new RuntimeException(s"Invalid property in body request: $key"))
        (field, value)
      }
      DatabaseTablesDAO.create(
        tableName = tableName,
        fieldsAndValues = fieldsAndValues,
        primaryKeyField = primaryKeyField,
        primaryKeyType = primaryKeyType
      )
    }
  }

  def update(tableName: String, primaryKeyValue: String, body: Map[String, String]): Future[Unit] =
    Future {
      database.withTransaction { implicit conn =>
        val settings = dataExplorerConfig.unsafeFindByName(tableName)
        val columns = DatabaseTablesDAO.getTableColumns(tableName)
        // hide non editable fields in case somebody edit it
        val bodyWithoutNonEditableColumns = body.filterNot { case (key, _) =>
          settings.nonEditableColumns.contains(key)
        }
        // transforms Map[String, String] to Map[TableColumn, String]
        // this is necessary because we want the column type to cast the data
        val fieldsAndValues = bodyWithoutNonEditableColumns.map { case (key, value) =>
          val field =
            columns.find(_.name == key).getOrElse(throw new RuntimeException(s"Invalid property in body request: $key"))
          (field, value)
        }
        val primaryKeyType = settings.primaryKeyDataType
        DatabaseTablesDAO.update(
          tableName = tableName,
          fieldsAndValues = fieldsAndValues,
          primaryKeyField = settings.primaryKeyField,
          primaryKeyValue = primaryKeyValue,
          primaryKeyType = primaryKeyType
        )
      }
    }

  def delete(tableName: String, primaryKeyValue: String): Future[Unit] =
    Future {
      database.withConnection { implicit conn =>
        val primaryKeyField = dataExplorerConfig.unsafeFindByName(tableName).primaryKeyField
        val primaryKeyType = dataExplorerConfig.unsafeFindByName(tableName).primaryKeyDataType
        DatabaseTablesDAO.delete(
          tableName = tableName,
          primaryKeyField = primaryKeyField,
          primaryKeyValue = primaryKeyValue,
          primaryKeyType = primaryKeyType
        )
      }
    }

  def numberOfRecords(tableName: String): Future[Int] = Future {
    database.withConnection { implicit conn =>
      DatabaseTablesDAO.countRecordsOnTable(tableName)
    }
  }

  def getImageData(
      settings: TableSettings,
      columnName: String,
      imageId: String
  ): Future[Option[Array[Byte]]] = Future {
    database.withConnection { implicit conn =>
      DatabaseTablesDAO.getImageData(settings, columnName, imageId)
    }
  }
}
