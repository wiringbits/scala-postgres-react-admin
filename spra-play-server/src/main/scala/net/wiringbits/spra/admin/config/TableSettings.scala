package net.wiringbits.spra.admin.config

import com.typesafe.config.Config
import net.wiringbits.spra.api.models.AdminGetTables.Response.ManyToOneReference
import play.api.ConfigLoader

import scala.util.Try

/** @param tableName
  *   name of table in database
  * @param primaryKeyField
  *   primary key identifier of table
  * @param referenceField
  *   field that react-admin shows for foreign key references instead of primary key
  * @param hiddenColumns
  *   columns that the API won't return when the data is queried (for example: user password)
  * @param nonEditableColumns
  *   columns that aren't editable (disabled) via react-admin
  * @param canBeDeleted
  *   indicates if resources from this table can be deleted
  * @param primaryKeyDataType
  *   UUID, Serial, or BigSerial primary keys
  * @param columnTypeOverrides
  *   overrides the data type and converts it, it requires a column name and Text, BinaryImage, Binary
  * @param filterableColumns
  *   columns that are filterable via react-admin
  */

case class CreateSettings(requiredColumns: List[String] = List.empty, nonRequiredColumns: List[String] = List.empty) {
  override def toString: String =
    s"CreateSettings(requiredColumns = $requiredColumns, nonRequiredColumns = $nonRequiredColumns)"
}

case class TableSettings(
    tableName: String,
    primaryKeyField: String,
    referenceField: Option[String] = None,
    hiddenColumns: List[String] = List.empty,
    nonEditableColumns: List[String] = List.empty,
    canBeDeleted: Boolean = true,
    primaryKeyDataType: PrimaryKeyDataType = PrimaryKeyDataType.UUID,
    columnTypeOverrides: Map[String, CustomDataType] = Map.empty,
    filterableColumns: List[String] = List.empty,
    createSettings: CreateSettings = CreateSettings(),
    referenceDisplayField: Option[String] = None,
    manyToOneReferences: List[ManyToOneReference] = List.empty
) {
  override def toString: String =
    s"""TableSettings(tableName = $tableName, primaryKeyField = $primaryKeyField, referenceField = $referenceField,
       hiddenColumns = $hiddenColumns, nonEditableColumns = $nonEditableColumns, canBeDeleted = $canBeDeleted,
       primaryKeyDataType = $primaryKeyDataType, columnTypeOverrides = $columnTypeOverrides,
       filterableColumns = $filterableColumns, createSettings = $createSettings, referenceDisplayField: $referenceDisplayField,
       manyToOneReferences: $manyToOneReferences)"""
}

object TableSettings {
  implicit val configLoader: ConfigLoader[TableSettings] = (config: Config, path: String) => {
    import scala.jdk.CollectionConverters.*
    val newConfig = config.getConfig(path)

    def get[A](path: String): A = {
      Try(newConfig.getAnyRef(path).asInstanceOf[A]).getOrElse(throw new RuntimeException(s"Cannot find $path"))
    }

    def getOption[A](path: String): Option[A] = {
      Try(newConfig.getAnyRef(path).asInstanceOf[A]).toOption
    }

    def getList[A](path: String): List[A] = {
      Try(newConfig.getAnyRefList(path).asScala.toList.asInstanceOf[List[A]]).getOrElse(List.empty)
    }

    def handleColumnTypeOverrides(): Map[String, CustomDataType] = {
      Try(
        newConfig
          .getConfig("columnTypeOverrides")
          .entrySet()
          .asScala
          .map { entry =>
            val value = entry.getValue.unwrapped().asInstanceOf[String]
            value match {
              case "BinaryImage" => entry.getKey -> CustomDataType.BinaryImage
              case "Binary" => entry.getKey -> CustomDataType.Binary
              case string => throw new RuntimeException(s"Invalid custom data type: $string")
            }
          }
          .toMap
      ).getOrElse(Map.empty)
    }

    def handleManyToOneReferences: List[ManyToOneReference] = Try(
      newConfig
        .getConfig("manyToOneReferences")
        .resolve()
        .entrySet()
        .asScala
        .map { entry =>
          entry.getKey.split('.').toList match
            case tableName :: _ =>
              val source = newConfig.getConfig(s"manyToOneReferences.$tableName").getString("source")
              val label = newConfig.getConfig(s"manyToOneReferences.$tableName").getString("label")
              ManyToOneReference(tableName, source, label)
            case _ => throw new RuntimeException(s"Invalid manyToOneReferences")
        }
        .toList
    ).getOrElse(List.empty)

    TableSettings(
      tableName = get[String]("tableName"),
      primaryKeyField = get[String]("primaryKeyField"),
      referenceField = getOption[String]("referenceField"),
      hiddenColumns = getList[String]("hiddenColumns"),
      nonEditableColumns = getList[String]("nonEditableColumns"),
      canBeDeleted = getOption[Boolean]("canBeDeleted").getOrElse(true),
      primaryKeyDataType = getOption[String]("primaryKeyDataType") match {
        case Some("UUID") | None => PrimaryKeyDataType.UUID
        case Some("Serial") => PrimaryKeyDataType.Serial
        case Some("BigSerial") => PrimaryKeyDataType.BigSerial
        case string => throw new RuntimeException(s"Invalid primary key data type: $string")
      },
      columnTypeOverrides = handleColumnTypeOverrides(),
      filterableColumns = getList[String]("filterableColumns"),
      createSettings = CreateSettings(
        requiredColumns = getList[String]("createFilter.requiredColumns"),
        nonRequiredColumns = getList[String]("createFilter.nonRequiredColumns")
      ),
      referenceDisplayField = getOption[String]("referenceDisplayField"),
      manyToOneReferences = handleManyToOneReferences
    )
  }
}

enum PrimaryKeyDataType {
  case UUID, Serial, BigSerial
}

enum CustomDataType {
  // TODO: add support to binary files
  case BinaryImage, Binary
}
