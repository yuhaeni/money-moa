package com.money.moa.securiy.user

import com.money.moa.member.domain.MemberRepository
import com.money.moa.securiy.CustomUserDetails
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(private val memberRepository: MemberRepository) : UserDetailsService {
    override fun loadUserByUsername(username: String): CustomUserDetails {
        val member = memberRepository.findByEmail(username) ?: throw UsernameNotFoundException("존재하지 않는 회원 입니다.")
        return CustomUserDetails(member.email, member.password, arrayListOf(SimpleGrantedAuthority(member.role)))
    }
}