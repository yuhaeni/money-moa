package com.money.moa.securiy

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class CustomUserDetails(
        val userName: String,
        val userPassword: String,
        val authorities: ArrayList<GrantedAuthority>
) : UserDetails {

    override fun getAuthorities(): MutableCollection<GrantedAuthority> {
        return authorities
    }

    override fun getPassword(): String {
        return userPassword
    }

    override fun getUsername(): String {
        return userName
    }
}