package cz.gregetom.graphwiki.commons.test.web

import cz.gregetom.graphwiki.commons.test.TestUsers
import cz.gregetom.graphwiki.commons.test.extensions.withOptionalUser
import org.springframework.security.core.userdetails.User
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.head
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.net.URI

class HttpHead(private val mockMvc: MockMvc) {

    fun doHead(uri: URI, expectedHeaders: Map<String, String>, testUser: User? = TestUsers.ADMIN) {
        val result = mockMvc.perform(head(uri).withOptionalUser(testUser))
                .andDo(print())
                .andExpect(status().isOk)
        expectedHeaders.forEach {
            result.andExpect(header().string(it.key, it.value))
        }
    }
}
