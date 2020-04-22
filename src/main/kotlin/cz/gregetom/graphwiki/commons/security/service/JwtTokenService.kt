package cz.gregetom.graphwiki.commons.security.service

import cz.gregetom.graphwiki.commons.security.enums.TokenClaims
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.GrantedAuthority
import org.springframework.stereotype.Component
import java.util.*
import java.util.function.Function

@Component
class JwtTokenService(
        @Value("\${graphwiki.security.jwt.secret}")
        private val secret: String
) {

    /**
     * Get username from JWT token.
     *
     * @param token JWT token
     * @return username
     */
    fun getUsernameFromToken(token: String?): String {
        return getClaimFromToken(token, Function { obj: Claims -> obj.subject })
    }

    /**
     * Get authorities from JWT token.
     *
     * @param token JWT token
     * @return user authorities
     */
    fun getAuthoritiesFromToken(token: String?): List<GrantedAuthority> {
        return (getClaimFromToken(
                token = token,
                claimsResolver = Function { obj: Claims -> obj.get(TokenClaims.AUTHORITIES) }) as List<*>)
                .map { GrantedAuthority { it.toString() } }
    }

    /**
     * Get user id from JWT token.
     *
     * @param token JWT token
     * @return user id
     */
    fun getUserIdFromToken(token: String?): String {
        return getClaimFromToken(
                token = token,
                claimsResolver = Function { obj: Claims -> obj.get(TokenClaims.USER_ID) as String })
    }

    /**
     * Generate JWT with specific claims and subject.
     *
     * @param claims token claims
     * @param subject token subject
     * @return generated JWT token
     */
    fun generateToken(claims: Map<String, Any>, subject: String): String {
        LOGGER.info("Generate token for $subject with claims $claims")
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(Date(System.currentTimeMillis()))
                .setExpiration(Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, secret).compact()
    }

    /**
     * Validate JWT token.
     *
     * @return true if token is valid, otherwise false
     */
    fun validateToken(token: String?, username: String): Boolean {
        return getUsernameFromToken(token) == username && !isTokenExpired(token)
    }

    /**
     * Get expiration date from JWT token.
     *
     * @param token JWT token
     * @return expiration date
     */
    private fun getExpirationDateFromToken(token: String?): Date {
        return getClaimFromToken(token, Function { obj: Claims -> obj.expiration })
    }

    private fun <T> getClaimFromToken(token: String?, claimsResolver: Function<Claims, T>): T {
        val claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).body
        return claimsResolver.apply(claims)
    }

    private fun isTokenExpired(token: String?): Boolean {
        val expiration = getExpirationDateFromToken(token)
        return expiration.before(Date())
    }

    companion object {
        const val JWT_TOKEN_VALIDITY = 5 * 60 * 60.toLong()
        private val LOGGER = LoggerFactory.getLogger(JwtTokenService::class.java)
    }
}
