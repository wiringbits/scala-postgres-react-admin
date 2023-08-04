package net.wiringbits.spra.admin.config

import play.api.Configuration

case class SpraConfig(tables: List[String]) {
  override def toString: String = {
    s"SpraConfig(tables = $tables)"
  }
}

object SpraConfig {

  def apply(config: Configuration): SpraConfig = {
//    val tables = config.get[Seq[String]]("tables")
//    println(config.keys)
    SpraConfig(config.subKeys.toList)
  }

}
