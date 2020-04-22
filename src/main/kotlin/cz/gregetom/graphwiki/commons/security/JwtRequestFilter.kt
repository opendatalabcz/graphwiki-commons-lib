package cz.gregetom.graphwiki.commons.security

import cz.gregetom.graphwiki.commons.security.service.JwtTokenService
import cz.gregetom.graphwiki.commons.security.util.HttpHeaderUtil
import io.jsonwebtoken.ExpiredJwtException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtRequestFilter(
        private val jwtTokenService: JwtTokenService
) : OncePerRequestFilter() {

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        val jwtToken: String? = HttpHeaderUtil.getTokenFromHttpRequest(request)
        var username: String? = null

        try {
            username = jwtTokenService.getUsernameFromToken(jwtToken)
        } catch (e: IllegalArgumentException) {
            logger.debug("Unable to get JWT Token")
        } catch (e: ExpiredJwtException) {
            logger.debug("JWT Token has expired")
        }

        // Once we get the token validate it.
        if (username != null && SecurityContextHolder.getContext().authentication == null) {
            // if token is valid configure Spring Security to manually set authentication
            if (jwtTokenService.validateToken(jwtToken, username)) {
                val usernamePasswordAuthenticationToken = UsernamePasswordAuthenticationToken(
                        jwtTokenService.getUserIdFromToken(jwtToken),
                        null,
                        jwtTokenService.getAuthoritiesFromToken(jwtToken)
                )
                usernamePasswordAuthenticationToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                // After setting the Authentication in the context, we specify that the current user is authenticated.
                // So it passes the Spring Security Configurations successfully.
                SecurityContextHolder.getContext().authentication = usernamePasswordAuthenticationToken
            }
        }
        chain.doFilter(request, response)
    }
}
