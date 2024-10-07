package com.money.moa.member.service

import com.money.moa.common.dto.ResponseDto
import com.money.moa.common.error.enums.CommonErrorCode
import com.money.moa.common.exception.CommonException
import com.money.moa.common.manager.MemberManager
import com.money.moa.member.domain.MemberRepository
import com.money.moa.member.dto.request.MemberLoginRequest
import com.money.moa.member.dto.request.MemberSaveRequest
import com.money.moa.member.dto.response.MemberFindResponse
import com.money.moa.member.dto.response.MemberSaveResponse
import com.money.moa.member.enums.MemberErrorCode
import com.money.moa.securiy.jwt.JwtProvider
import com.money.moa.securiy.user.CustomUserDetailsService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class MemberService @Autowired constructor(
        private val memberRepository: MemberRepository,
        private val passwordEncoder: PasswordEncoder,
        private val memberManager: MemberManager,
        private val userDetailsService: CustomUserDetailsService,
        private val jwtProvider: JwtProvider,
) {

    fun login(response: HttpServletResponse, memberLoginRequest: MemberLoginRequest): ResponseEntity<ResponseDto<*>> {
        val userDetails = userDetailsService.loadUserByUsername(memberLoginRequest.email)
        val isMatches = passwordEncoder.matches(memberLoginRequest.password, userDetails.password)
        if (!isMatches) {
            throw CommonException(MemberErrorCode.INVALID_PASSWORD)
        }

        val claims = jwtProvider.buildClaims(userDetails.userName, AuthorityUtils.authorityListToSet(userDetails.authorities))
        jwtProvider.issueToken(response, claims)

        return ResponseDto.ok()
    }

    fun saveMember(memberSaveRequest: MemberSaveRequest): ResponseEntity<ResponseDto<MemberSaveResponse>> {
        // 검증 및 exception 발생
        validateSaveMember(memberSaveRequest)
        memberSaveRequest.password = passwordEncoder.encode(memberSaveRequest.password)

        // 엔티티 저장
        val savedMember = memberRepository.save(memberSaveRequest.toEntity())

        // 응답 객체로 변경하여 리턴
        return ResponseDto.created(savedMember.toDto())
    }

    private fun validateSaveMember(memberSaveRequest: MemberSaveRequest) {
        if (!memberManager.checkDuplicateMemberEmail(memberSaveRequest.email)) {
            throw CommonException(MemberErrorCode.DUPLICATE_EMAIL)
        }

        if (!memberManager.checkMemberPassword(memberSaveRequest.password)) {
            throw CommonException(MemberErrorCode.INVALID_PASSWORD)
        }
    }

    fun logout(httpServletRequest: HttpServletRequest): ResponseEntity<ResponseDto<*>> {
        try {
            jwtProvider.setBlackListToken(httpServletRequest)
        } catch (e: Exception) {
            throw CommonException(CommonErrorCode.INVALID_PARAMETER)
        }

        return ResponseDto.ok()
    }
}