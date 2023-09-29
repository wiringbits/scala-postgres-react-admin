package net.wiringbits.spra.admin.utils

import net.wiringbits.spra.admin.config.PrimaryKeyDataType
import net.wiringbits.spra.admin.repositories.models.TableColumn
import net.wiringbits.spra.admin.utils.models.QueryParameters

import scala.collection.mutable

object QueryBuilder {
  def get(
      tableName: String,
      fieldsAndValues: Map[TableColumn, String],
      queryParameters: QueryParameters,
      primaryKeyField: String
  ): String = {
    val filters = for {
      (tableColumn, _) <- fieldsAndValues
    } yield
    // It is ideal to convert timestamptz to date when comparing dates to avoid the time
    if tableColumn.`type` == "timestamptz" then s"${tableColumn.name}::date = ?::date"
    else s"${tableColumn.name} = ?::${tableColumn.`type`}"

    // react-admin gives us a "id" field instead of the primary key of the actual column so we need to replace it
    val sortBy = if (queryParameters.sort.field == "id") primaryKeyField else queryParameters.sort.field
    val limit = queryParameters.pagination.end - queryParameters.pagination.start
    val offset = queryParameters.pagination.start

    s"""
      |SELECT *
      |FROM $tableName
      |${if filters.nonEmpty then filters.mkString("WHERE ", " AND ", " ") else ""}
      |ORDER BY $sortBy ${queryParameters.sort.ordering}
      |LIMIT $limit OFFSET $offset
      |""".stripMargin
  }

  def create(
      tableName: String,
      fieldsAndValues: Map[TableColumn, String],
      primaryKeyField: String,
      primaryKeyType: PrimaryKeyDataType = PrimaryKeyDataType.UUID
  ): String = {
    val sqlFields = new mutable.StringBuilder(primaryKeyField)
    val sqlValues = primaryKeyType match {
      case PrimaryKeyDataType.UUID => new mutable.StringBuilder("?")
      case PrimaryKeyDataType.Serial => new mutable.StringBuilder("DEFAULT")
      case PrimaryKeyDataType.BigSerial => new mutable.StringBuilder("DEFAULT")
    }
    for ((tableColumn, _) <- fieldsAndValues) {
      sqlFields.append(s", ${tableColumn.name}")
      sqlValues.append(s", ?::${tableColumn.`type`}")
    }

    s"""
      |INSERT INTO $tableName
      |  ($sqlFields)
      |VALUES (
      |  ${sqlValues.toString()}
      |)
      |RETURNING $primaryKeyField::TEXT
      |""".stripMargin
  }

  def update(tableName: String, body: Map[TableColumn, String], primaryKeyField: String): String = {
    val updateStatement = new mutable.StringBuilder("SET")
    for ((tableField, value) <- body) {
      val resultStatement = if (value == "null") "NULL" else s"?::${tableField.`type`}"
      val statement = s" ${tableField.name} = $resultStatement,"
      updateStatement.append(statement)
    }
    updateStatement.deleteCharAt(updateStatement.length - 1)
    s"""
    |UPDATE $tableName
    |$updateStatement
    |WHERE $primaryKeyField = ?
    |""".stripMargin
  }
}
