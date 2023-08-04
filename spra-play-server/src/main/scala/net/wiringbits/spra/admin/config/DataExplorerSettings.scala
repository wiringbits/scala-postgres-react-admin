package net.wiringbits.spra.admin.config

class DataExplorerSettings(val baseUrl: String, val tables: List[TableSettings]) {
  // TODO: remove this constructor
  def this() = this("", List.empty)

  def unsafeFindByName(tableName: String): TableSettings = {
    tables
      .find(_.tableName == tableName)
      .getOrElse(throw new RuntimeException(s"Cannot find settings for table: $tableName"))
  }
}
