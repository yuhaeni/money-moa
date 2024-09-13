package com.money.moa.securiy

import com.money.moa.common.enums.Role
import com.money.moa.member.domain.Member
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class CustomUserDetails (
        val userName: String,
        val userPassword: String,
        val authorities: ArrayList<GrantedAuthority>
) : UserDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return authorities
    }


    override fun getPassword(): String {
        return userPassword
    }

    override fun getUsername(): String {
        return userName
    }
}