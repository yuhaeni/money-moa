package com.money.moa.securiy.user

import com.money.moa.common.error.enums.CommonErrorCode
import com.money.moa.common.exception.CommonException
import com.money.moa.member.domain.MemberRepository
import com.money.moa.member.enums.MemberErrorCode
import com.money.moa.securiy.CustomUserDetails
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(private val memberRepository: MemberRepository) : UserDetailsService {
    override fun loadUserByUsername(username: String): CustomUserDetails {
        val member = memberRepository.findByEmail(username)
        if (member == null) {
            throw CommonException(MemberErrorCode.NOT_FOUND_MEMBER)
        }

        return CustomUserDetails(member.email, member.password, arrayListOf(SimpleGrantedAuthority(member.role)))
    }
}