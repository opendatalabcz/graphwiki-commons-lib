package cz.gregetom.graphwiki.commons.test

import cz.gregetom.graphwiki.commons.security.enums.Roles
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User

object TestUsers {
    val ADMIN = User("admin", "admin", listOf(GrantedAuthority { Roles.ROLE_ADMIN }))
    val ADMIN_ANOTHER = User("admin_another", "admin_another", listOf(GrantedAuthority { Roles.ROLE_ADMIN }))
    val USER = User("user", "user", listOf(GrantedAuthority { Roles.ROLE_USER }))
    val USER_ANOTHER = User("user_another", "user_another", listOf(GrantedAuthority { Roles.ROLE_USER }))
    val TECHNICAL = User("technical", "technical", listOf(GrantedAuthority { Roles.ROLE_TECHNICAL }))
}
