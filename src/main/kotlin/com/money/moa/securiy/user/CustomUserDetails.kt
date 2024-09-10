package com.money.moa.securiy

import com.money.moa.common.enums.Role
import com.money.moa.member.domain.Member
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class CustomUserDetails(
        val userName: String,
        val userPassword: String,
        val role: Role
) : UserDetails {

    companion object {
        fun from(member: Member): CustomUserDetails {
            return with(member) {
                CustomUserDetails(
                        userName = email,
                        userPassword = password,
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
        return userPassword
    }

    override fun getUsername(): String {
        return userName
    }
}