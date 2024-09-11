package net.wiringbits.spra.admin.models

sealed trait ColumnType {
  val value: String
}

object ColumnType {
  case class Date(value: String) extends ColumnType
  case class Text(value: String) extends ColumnType
  case class Bytea(value: String) extends ColumnType
  case class Int(value: String) extends ColumnType
  case class Decimal(value: String) extends ColumnType
  case class UUID(value: String) extends ColumnType

  def parseColumnType(columnType: String): ColumnType = {
    // 'contains' is used because PostgreSQL types may include additional details like precision or scale
    // https://www.postgresql.org/docs/8.1/datatype.html
    val isInt = List("int", "serial").exists(columnType.contains)
    val isDecimal = List("float", "decimal").exists(columnType.contains)
    val isBytea = columnType == "bytea"
    val isUUID = columnType == "uuid"
    val isDate = List("date", "time").exists(columnType.contains)

    if (isDate)
      Date(columnType)
    else if (isDecimal)
      Decimal(columnType)
    else if (isBytea)
      Bytea(columnType)
    else if (isInt)
      Int(columnType)
    else if (isUUID)
      UUID(columnType)
    else
      Text(columnType)
  }
}
