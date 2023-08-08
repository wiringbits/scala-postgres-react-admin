package net.wiringbits.spra.admin.modules

import com.google.inject.{AbstractModule, Provides, Singleton}
import net.wiringbits.spra.admin.config.DataExplorerConfig
import org.slf4j.LoggerFactory
import play.api.Configuration

class ConfigModule extends AbstractModule {

  private val logger = LoggerFactory.getLogger(this.getClass)

  @Provides
  @Singleton
  def dataExplorerConfig(global: Configuration): DataExplorerConfig = {
    val config = DataExplorerConfig(global.get[Configuration]("dataExplorer"))
    logger.info(s"Loaded configuration: $config")
    config
  }

}
