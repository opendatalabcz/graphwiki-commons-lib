package cz.gregetom.graphwiki.commons.web

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.http.client.ClientHttpResponse
import org.springframework.stereotype.Component
import org.springframework.web.client.DefaultResponseErrorHandler
import org.springframework.web.client.RestTemplate
import org.springframework.web.server.ResponseStatusException
import java.net.URI

/**
 * Allow communication between services.
 *
 * @param technicalJwtToken permanent JWT token
 */
@Component
class InterCommunicationRestTemplate(
        @Value("\${graphwiki.security.jwt.technical-token}")
        private val technicalJwtToken: String) : RestTemplate() {

    init {
        super.setErrorHandler(object : DefaultResponseErrorHandler() {
            override fun handleError(response: ClientHttpResponse) {
                LOGGER.error("Inter communication ${response.rawStatusCode} error occurs, message: '${response.statusText}'")
                throw ResponseStatusException(response.statusCode, "Inter communication ${response.rawStatusCode} error occurs, message: '${response.statusText}'")
            }
        })
    }

    fun <T : Any?> getAsTechnicalUser(uri: URI, responseType: Class<T>): T? {
        LOGGER.info("Call GET method on $uri, expected response type $responseType")
        val headers = HttpHeaders()
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer $technicalJwtToken")
        return super.exchange(uri, HttpMethod.GET, HttpEntity<Unit>(headers), responseType).body
    }

    fun putAsTechnicalUser(uri: URI) {
        LOGGER.info("Call POST method on $uri")
        val headers = HttpHeaders()
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer $technicalJwtToken")
        super.put(uri, HttpEntity<Unit>(headers))
    }

    fun <T> postForLocationAsTechnicalUser(uri: URI, entity: T): URI {
        LOGGER.info("Call POST method on $uri and entity $entity")
        val headers = HttpHeaders()
        headers.setContentType(MediaType.APPLICATION_JSON)
        headers.setAccept(listOf(MediaType.APPLICATION_JSON))
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer $technicalJwtToken")
        val request = HttpEntity<T>(entity, headers)
        return super.postForLocation(uri.toString(), request)
                ?: throw IllegalStateException("No location returned from url $uri")

    }

    fun deleteAsTechnicalUser(uri: URI) {
        LOGGER.info("Call DELETE method on $uri")
        val headers = HttpHeaders()
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer $technicalJwtToken")
        super.exchange(uri, HttpMethod.DELETE, HttpEntity<Unit>(headers), Unit::class.java)
    }

    private companion object {
        private val LOGGER = LoggerFactory.getLogger(InterCommunicationRestTemplate::class.java)
    }
}
