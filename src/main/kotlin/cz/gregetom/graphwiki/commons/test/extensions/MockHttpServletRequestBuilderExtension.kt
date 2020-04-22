package cz.gregetom.graphwiki.commons.test.extensions

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.MediaType
import org.springframework.security.core.userdetails.User
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder

fun <T> MockHttpServletRequestBuilder.withBody(entity: T, objectMapper: ObjectMapper): MockHttpServletRequestBuilder {
    return this.contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(entity))
}

fun MockHttpServletRequestBuilder.withOptionalUser(user: User?): MockHttpServletRequestBuilder {
    return user?.let { this.with(SecurityMockMvcRequestPostProcessors.user(it)) } ?: this
}
