package com.money.moa.member.service

import com.money.moa.common.dto.ResponseDto
import com.money.moa.common.exception.CommonException
import com.money.moa.common.manager.MemberManager
import com.money.moa.member.domain.MemberRepository
import com.money.moa.member.dto.request.MemberLoginRequest
import com.money.moa.member.dto.request.MemberSaveRequest
import com.money.moa.member.dto.response.MemberFindResponse
import com.money.moa.member.dto.response.MemberResponse
import com.money.moa.redis.util.RedisUtil
import com.money.moa.securiy.jwt.JwtProvider
import com.money.moa.securiy.user.CustomUserDetailsService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.context.SecurityContextHolder
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

        val claims = jwtProvider.buildClaims(userDetails.userName, AuthorityUtils.authorityListToSet(userDetails.authorities))
        jwtProvider.issueToken(response, claims)

        return ResponseDto.ok()
    }

    fun findMember(memberFindRequest: MemberLoginRequest): MemberFindResponse? {
        val member = memberRepository.findByEmail(memberFindRequest.email)
                ?: throw CommonException(HttpStatus.NOT_FOUND, "member not found")
        val isMatches = passwordEncoder.matches(memberFindRequest.password, member.password)
        if (!isMatches) {
            return null
        }

        return member.fromEntity()
    }

    fun saveMember(memberSaveRequest: MemberSaveRequest): ResponseEntity<ResponseDto<MemberResponse>> {
        // 검증 및 exception 발생
        validateSaveMember(memberSaveRequest)
        memberSaveRequest.password = passwordEncoder.encode(memberSaveRequest.password)

        // 엔티티 저장
        val savedMember = memberRepository.save(memberSaveRequest.toEntity())

        // 응답 객체로 변경하여 리턴
        return ResponseDto.created(savedMember.toResponse())
    }

    private fun validateSaveMember(memberSaveRequest: MemberSaveRequest) {
        if (memberManager.checkDuplicateMemberEmail(memberSaveRequest.email)) {
            throw CommonException(HttpStatus.BAD_REQUEST, "이미 등록된 이메일입니다.")
        }

        if (!memberManager.checkMemberPassword(memberSaveRequest.password)) {
            throw CommonException(HttpStatus.BAD_REQUEST, "유효하지 않은 비밀번호입니다.")
        }
    }

    fun logout(httpServletRequest: HttpServletRequest): ResponseEntity<ResponseDto<*>> {
        try {
            jwtProvider.setBlackListToken(httpServletRequest)
        } catch (e: Exception) {
            throw CommonException(HttpStatus.BAD_REQUEST, e.message.toString())
        }

        return ResponseDto.ok()
    }
}