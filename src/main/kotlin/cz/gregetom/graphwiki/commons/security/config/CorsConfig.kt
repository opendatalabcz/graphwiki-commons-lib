package cz.gregetom.graphwiki.commons.security.config

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

/**
 * Allow CORS for specific origins.
 *
 * @param corsAllowedOrigins specified origins to be allowed
 */
@Configuration
open class CorsConfig(
        @Value("\${graphwiki.web.cors.allowed-origins}")
        private val corsAllowedOrigins: Array<String>
) {

    @Bean
    open fun corsConfigurer(): WebMvcConfigurer {
        LOGGER.info("Register CORS, allowed origins: ${corsAllowedOrigins.joinToString()}")
        return object : WebMvcConfigurer {
            override fun addCorsMappings(registry: CorsRegistry) {
                registry.addMapping("/**")
                        .allowedOrigins(*corsAllowedOrigins)
                        .allowedMethods(*HttpMethod.values().map { it.name }.toTypedArray())
            }
        }
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(CorsConfig::class.java)
    }
}
