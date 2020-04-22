package cz.gregetom.graphwiki.commons.test.web

import com.fasterxml.jackson.databind.ObjectMapper
import cz.gregetom.graphwiki.commons.test.TestUsers
import cz.gregetom.graphwiki.commons.test.extensions.withOptionalUser
import org.springframework.http.HttpStatus
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.security.core.userdetails.User
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.net.URI
import kotlin.reflect.KClass

class HttpGet(private val mockMvc: MockMvc, private val objectMapper: ObjectMapper) {

    fun doGetAndReturnResponse(uri: URI, testUser: User? = TestUsers.ADMIN): MockHttpServletResponse {
        return mockMvc.perform(get(uri).withOptionalUser(testUser))
                .andDo(print())
                .andExpect(status().isOk)
                .andReturn()
                .response
    }

    fun <T : Any> doGet(clazz: KClass<T>, uri: URI, testUser: User? = TestUsers.ADMIN): T {
        return objectMapper.readValue(
                doGetAndReturnResponse(uri, testUser).contentAsString,
                clazz.java
        )
    }

    fun doGetAndExpect(uri: URI, expectedStatus: HttpStatus, testUser: User? = TestUsers.ADMIN) {
        mockMvc.perform(get(uri).withOptionalUser(testUser))
                .andDo(print())
                .andExpect(status().`is`(expectedStatus.value()))
    }

    fun <T : Any> doGet(clazz: KClass<T>,
                        url: String,
                        testUser: User? = TestUsers.ADMIN): T {
        return doGet(clazz, URI(url), testUser)
    }
}
