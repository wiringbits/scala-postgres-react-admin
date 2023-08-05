package net.wiringbits.spra.admin.config

import play.api.Configuration

case class DataExplorerConfig(baseUrl: String, tablesSettings: List[TableSettings]) {
  def unsafeFindByName(tableName: String): TableSettings = {
    tablesSettings
      .find(_.tableName == tableName)
      .getOrElse(throw new RuntimeException(s"Cannot find config for table: $tableName"))
  }
}

object DataExplorerConfig {
  def apply(config: Configuration): DataExplorerConfig = {
    val baseUrl = config.get[String]("baseUrl")
    val tableNames = config.subKeys.toList.filter(_ != "baseUrl")
    val tablesSettings = tableNames.map(config.get[TableSettings](_))
    DataExplorerConfig(baseUrl, tablesSettings)
  }
}
