package cz.gregetom.graphwiki.commons.test.web

import com.fasterxml.jackson.databind.ObjectMapper
import cz.gregetom.graphwiki.commons.test.TestUsers
import cz.gregetom.graphwiki.commons.test.extensions.withBody
import cz.gregetom.graphwiki.commons.test.extensions.withOptionalUser
import org.springframework.http.HttpStatus
import org.springframework.security.core.userdetails.User
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.net.URI

class HttpPut(private val mockMvc: MockMvc, private val objectMapper: ObjectMapper) {

    fun <T : Any> doPut(uri: URI, entity: T, testUser: User? = TestUsers.ADMIN) {
        this.doPut(uri.toString(), entity, testUser)
    }

    fun <T : Any> doPut(url: String, entity: T, testUser: User? = TestUsers.ADMIN) {
        mockMvc.perform(put(url).withOptionalUser(testUser)
                .withBody(entity, objectMapper))
                .andDo(print())
                .andExpect(status().isOk)
    }


    fun <T : Any> doPutAndExpect(url: String, entity: T, expectedStatus: HttpStatus, testUser: User? = TestUsers.ADMIN) {
        mockMvc.perform(put(url).withOptionalUser(testUser)
                .withBody(entity, objectMapper))
                .andDo(print())
                .andExpect(status().`is`(expectedStatus.value()))
    }

    fun <T : Any> doPutAndExpect(uri: URI, entity: T, expectedStatus: HttpStatus, testUser: User? = TestUsers.ADMIN) {
        this.doPutAndExpect(uri.toString(), entity, expectedStatus, testUser)
    }

    fun doPutAndExpect(uri: URI, expectedStatus: HttpStatus, testUser: User? = TestUsers.ADMIN) {
        mockMvc.perform(put(uri).withOptionalUser(testUser))
                .andDo(print())
                .andExpect(status().`is`(expectedStatus.value()))
    }


    fun doPutEmptyBody(url: String, testUser: User? = TestUsers.ADMIN) {
        mockMvc.perform(put(url).withOptionalUser(testUser))
                .andDo(print())
                .andExpect(status().isOk)
    }

    fun doPutEmptyBody(uri: URI, testUser: User? = TestUsers.ADMIN) {
        this.doPutEmptyBody(uri.toString(), testUser)
    }
}
