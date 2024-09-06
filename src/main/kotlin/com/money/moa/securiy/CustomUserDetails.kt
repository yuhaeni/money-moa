package com.money.moa.securiy

import com.money.moa.common.enums.Role
import com.money.moa.member.domain.Member
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class CustomUserDetails(
        private val userName: String,
        private val password: String,
        private val role: Role
) : UserDetails {

    companion object {
        fun from(member: Member): CustomUserDetails {
            return with(member) {
                CustomUserDetails(
                        userName = email,
                        password = password,
                        role = role,
                )
            }
        }
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        val authorities = ArrayList<GrantedAuthority>()
        authorities.add(SimpleGrantedAuthority(role.toString()))
        return authorities
    }

    override fun getPassword(): String {
        return password
    }

    override fun getUsername(): String {
        return userName
    }
}