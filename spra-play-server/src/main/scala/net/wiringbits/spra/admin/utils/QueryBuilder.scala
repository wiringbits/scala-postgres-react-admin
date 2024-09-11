package net.wiringbits.spra.admin.utils

import net.wiringbits.spra.admin.config.PrimaryKeyDataType
import net.wiringbits.spra.admin.repositories.models.TableColumn

import scala.collection.mutable

object QueryBuilder {
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
      sqlValues.append(s", ?::${tableColumn.`type`.value}")
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
      val resultStatement = if (value == "null") "NULL" else s"?::${tableField.`type`.value}"
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
