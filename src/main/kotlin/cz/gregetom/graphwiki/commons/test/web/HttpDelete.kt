package cz.gregetom.graphwiki.commons.test.web

import cz.gregetom.graphwiki.commons.test.TestUsers
import cz.gregetom.graphwiki.commons.test.extensions.withOptionalUser
import org.springframework.http.HttpStatus
import org.springframework.security.core.userdetails.User
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.net.URI

class HttpDelete(private val mockMvc: MockMvc) {

    fun doDelete(uri: URI, testUser: User? = TestUsers.ADMIN) {
        this.doDelete(uri.toString(), testUser)
    }

    fun doDelete(url: String, testUser: User? = TestUsers.ADMIN) {
        mockMvc.perform(MockMvcRequestBuilders.delete(url).withOptionalUser(testUser))
                .andDo(print())
                .andExpect(status().isOk)
    }

    fun doDeleteAndExpect(uri: URI, expectedStatus: HttpStatus, testUser: User? = TestUsers.ADMIN) {
        this.doDeleteAndExpect(uri.toString(), expectedStatus, testUser)
    }

    fun doDeleteAndExpect(url: String, expectedStatus: HttpStatus, testUser: User? = TestUsers.ADMIN) {
        mockMvc.perform(MockMvcRequestBuilders.delete(url).withOptionalUser(testUser))
                .andDo(print())
                .andExpect(status().`is`(expectedStatus.value()))
    }
}
