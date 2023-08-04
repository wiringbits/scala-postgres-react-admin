package net.wiringbits.spra.admin.modules

import com.google.inject.{AbstractModule, Provides, Singleton}
import net.wiringbits.spra.admin.config.SpraConfig
import org.slf4j.LoggerFactory
import play.api.Configuration

class ConfigModule extends AbstractModule {

  private val logger = LoggerFactory.getLogger(this.getClass)

  @Provides
  @Singleton
  def provideSpraConfig(global: Configuration): SpraConfig = {
    val config = SpraConfig(global.get[Configuration]("spra"))
    logger.info(s"Loaded configuration: $config")
    config
  }

}
