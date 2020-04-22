package cz.gregetom.graphwiki.commons.test.web

import com.fasterxml.jackson.databind.ObjectMapper
import cz.gregetom.graphwiki.commons.test.TestUsers
import cz.gregetom.graphwiki.commons.test.extensions.withBody
import cz.gregetom.graphwiki.commons.test.extensions.withOptionalUser
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.security.core.userdetails.User
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.net.URI
import kotlin.reflect.KClass

class HttpPost(private val mockMvc: MockMvc, private val objectMapper: ObjectMapper) {

    fun <T : Any> doPost(uri: URI, entity: T, testUser: User? = TestUsers.ADMIN): String {
        return doPost(uri.toString(), entity, testUser)
    }

    fun <T : Any> doPost(url: String, entity: T, testUser: User? = TestUsers.ADMIN): String {
        val location = mockMvc.perform(post(url).withOptionalUser(testUser)
                .withBody(entity, objectMapper))
                .andDo(print())
                .andExpect(status().isCreated)
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andReturn()
                .response.getHeader(HttpHeaders.LOCATION)
        require(location != null) { "Should not happened!" }
        return location
    }

    fun <T : Any, U : Any> doPostForObject(clazz: KClass<T>, uri: URI, entity: U, testUser: User? = TestUsers.ADMIN): T {
        return objectMapper.readValue(
                mockMvc.perform(post(uri).withOptionalUser(testUser)
                        .withBody(entity, objectMapper))
                        .andDo(print())
                        .andExpect(status().isOk)
                        .andReturn()
                        .response
                        .contentAsString,
                clazz.java
        )
    }

    fun <T : Any> doPostAndExpect(uri: URI, entity: T, expectedStatus: HttpStatus, testUser: User? = TestUsers.ADMIN) {
        this.doPostAndExpect(uri.toString(), entity, expectedStatus, testUser)
    }

    fun <T : Any> doPostAndExpect(url: String, entity: T, expectedStatus: HttpStatus, testUser: User? = TestUsers.ADMIN) {
        mockMvc.perform(post(url).withOptionalUser(testUser)
                .withBody(entity, objectMapper))
                .andDo(print())
                .andExpect(status().`is`(expectedStatus.value()))
    }
}
