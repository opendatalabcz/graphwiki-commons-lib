package cz.gregetom.graphwiki.commons.security.util

import org.springframework.http.HttpHeaders
import javax.servlet.http.HttpServletRequest

object HttpHeaderUtil {

    /**
     * Resolve JWT token from http request.
     *
     * @param request http request
     * @return JWT token parsed from request
     */
    fun getTokenFromHttpRequest(request: HttpServletRequest): String? {
        val requestTokenHeader = request.getHeader(HttpHeaders.AUTHORIZATION)
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            return requestTokenHeader.removePrefix("Bearer ")
        }
        return null
    }
}
