package com.money.moa.securiy.user

import com.money.moa.member.domain.MemberRepository
import com.money.moa.securiy.CustomUserDetails
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(private val memberRepository: MemberRepository) : UserDetailsService {
    // 출처:: https://anomie7.tistory.com/65
    override fun loadUserByUsername(username: String): CustomUserDetails {
        return memberRepository.findByEmail(username)?.let { CustomUserDetails.from(it) }
                ?: throw UsernameNotFoundException("$username Can Not Found")
    }
}