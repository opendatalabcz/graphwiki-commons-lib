package cz.gregetom.graphwiki.commons.security.util

import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder

/**
 * Provides information about user from security context
 */
object UserAccessor {

    /**
     * Find out if current user has any role from provided list.
     *
     * @param roles list of roles to be checked
     * @return true if current user has any role, otherwise false
     */
    fun hasRoleAny(vararg roles: String): Boolean {
        return currentUserRoles.intersect(roles.asList()).isNotEmpty()
    }

    /**
     * @return true if current user is logged in and [currentUserId] is equal to [userId], otherwise false
     */
    fun currentUserIs(userId: String?): Boolean {
        return userId !== null && this.currentUserId !== null && userId == this.currentUserIdOrThrow
    }

    /**
     * @return true if current user is logged in and [currentUserId] is not equal to [userId], otherwise false
     */
    fun currentUserIsNot(userId: String?): Boolean {
        return this.currentUserId !== null && this.currentUserId != userId
    }

    /**
     * @return true if user is logged, otherwise false
     */
    val isLogged: Boolean
        get() {
            return currentUserId !== null
        }

    /**
     * Get current user id.
     * If user is not logged in, return null.
     *
     * @return current user id or null
     */
    val currentUserId: String?
        get() {
            return getAuthentication?.name
        }

    /**
     * Get current user id.
     *
     * @throws IllegalStateException if user is not logged in
     * @return current user id
     */
    val currentUserIdOrThrow: String
        get() {
            return getAuthentication?.name ?: throw IllegalStateException("Security context is empty!")
        }

    /**
     * Get current user roles.
     *
     * @throws IllegalStateException if user is not logged in
     * @return current user roles
     */
    val currentUserRolesOrThrow: Set<String>
        get() {
            return getAuthentication
                    ?.let { it.authorities.map { it.authority }.toSet() }
                    ?: throw IllegalStateException("Security context is empty!")
        }

    /**
     * Get current roles.
     * If user is not logged in, return empty list.
     *
     * @return current user roles or empty
     */
    private val currentUserRoles: Set<String>
        get() {
            return getAuthentication
                    ?.let { it.authorities.map { it.authority }.toSet() }
                    ?: emptySet()
        }

    private val getAuthentication: Authentication?
        get() {
            val authentication = SecurityContextHolder.getContext().authentication
            return if (authentication !is AnonymousAuthenticationToken) authentication else null
        }
}
