package io.terra.yamalHack.config

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.support.PropertiesLoaderUtils
import org.springframework.stereotype.Component

@Component
@PropertySource("classpath:cdn.properties")
class GcsConfig {

    final val active: String
    final val hostname: String
    final val canonicalUrl: String
    final val configFilePath: String
    final val serverUrl: String

    init {
        val resource = ClassPathResource("/cdn.properties")
        val props = PropertiesLoaderUtils.loadProperties(resource)


        active = props.getProperty("gcs.active")
        hostname = props.getProperty("gcs.hostname")
        canonicalUrl = props.getProperty("gcs.canonicalUrl")
        configFilePath = props.getProperty("gcs.configFile.path")
        serverUrl = props.getProperty("server.url")
    }

}