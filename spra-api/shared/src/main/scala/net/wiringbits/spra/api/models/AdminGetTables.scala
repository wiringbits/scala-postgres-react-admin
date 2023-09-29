package net.wiringbits.spra.api.models

import play.api.libs.json.{Format, Json}

object AdminGetTables {
  case class Response(data: List[Response.DatabaseTable])
  object Response {
    case class DatabaseTable(
        name: String,
        columns: List[TableColumn],
        primaryKeyName: String,
        canBeDeleted: Boolean,
        referenceDisplayField: Option[String],
        manyToOneReferences: List[ManyToOneReference]
    )
    case class TableColumn(
        name: String,
        `type`: String,
        editable: Boolean,
        reference: Option[TableReference],
        filterable: Boolean,
        isVisible: Boolean,
        isRequiredOnCreate: Option[Boolean]
    )
    case class TableReference(referencedTable: String, referenceField: String)
    case class ManyToOneReference(tableName: String, source: String, label: String)

    implicit val adminTableReferenceResponseFormat: Format[TableReference] = Json.format[TableReference]
    implicit val adminTableColumnResponseFormat: Format[TableColumn] = Json.format[TableColumn]
    implicit val adminManyToOneReferenceResponseFormat: Format[ManyToOneReference] = Json.format[ManyToOneReference]
    implicit val adminDatabaseTableResponseFormat: Format[DatabaseTable] = Json.format[DatabaseTable]
  }
  implicit val adminGetTablesResponseFormat: Format[Response] = Json.format[Response]
}
