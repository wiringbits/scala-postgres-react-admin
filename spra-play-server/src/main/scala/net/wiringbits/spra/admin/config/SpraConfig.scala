package net.wiringbits.spra.admin.config

import play.api.Configuration

case class SpraConfig(tablesSettings: List[TableSettings]) {
  override def toString: String = {
    s"SpraConfig(tablesSettings = $tablesSettings)"
  }
}

object SpraConfig {
  def apply(config: Configuration): SpraConfig = {
    val tableNames = config.subKeys.toList
    val tablesSettings = tableNames.map(config.get[TableSettings](_))
    SpraConfig(tablesSettings)
  }

}
