package cz.gregetom.graphwiki.commons.web

import org.springframework.hateoas.Link
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI

object LinkBuilder {

    /**
     * Create link to another service.
     *
     * @param serviceBaseUrl url of target service
     * @param invocation target controller method
     * @param rel link rel
     * @return link
     */
    fun <T> anotherServiceLink(serviceBaseUrl: URI,
                               invocation: T,
                               rel: String): Link {
        val uri = linkTo(invocation).toUri()
        return Link(
                UriComponentsBuilder.fromUri(serviceBaseUrl)
                        // remove servlet context path
                        .path(uri.path.removePrefix("/").substringAfter("/").prependIndent("/"))
                        .query(uri.query)
                        .build()
                        .toUriString(),
                rel
        )
    }
}
