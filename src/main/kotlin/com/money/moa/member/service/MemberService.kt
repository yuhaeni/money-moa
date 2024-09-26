package com.money.moa.member.service

import com.common.dto.ResponseDto
import com.common.exception.customs.BizException
import com.money.moa.common.manager.MemberManager
import com.money.moa.member.domain.MemberRepository
import com.money.moa.member.dto.request.MemberFindRequest
import com.money.moa.member.dto.request.MemberSaveRequest
import com.money.moa.member.dto.response.MemberFindResponse
import com.money.moa.member.dto.response.MemberResponse
import com.money.moa.securiy.jwt.JwtProvider
import com.money.moa.securiy.user.CustomUserDetailsService
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
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
        private val jwtProvider: JwtProvider
) {

    fun login(response: HttpServletResponse, memberFindRequest: MemberFindRequest): ResponseEntity<ResponseDto<*>> {
        val userDetails = userDetailsService.loadUserByUsername(memberFindRequest.email)

        val claims = jwtProvider.buildClaims(userDetails.userName, AuthorityUtils.authorityListToSet(userDetails.authorities))
        jwtProvider.issueToken(response, claims)

        return ResponseDto.ok()
    }

    fun findMember(memberFindRequest: MemberFindRequest): MemberFindResponse? {
        val member = memberRepository.findByEmail(memberFindRequest.email)
                ?: throw IllegalStateException("member not found")
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
            // throw BizException("이미 등록된 이메일입니다.") -> 상속된 RuntimeException 때문에 InternalServerError 발생 (500) DefaultExceptionHandler 확인 필요
            throw BizException(HttpStatus.BAD_REQUEST, "이미 등록된 이메일입니다.")
        }

        if (!memberManager.checkMemberPassword(memberSaveRequest.password)) {
            throw BizException(HttpStatus.BAD_REQUEST, "유효하지 않은 비밀번호입니다.")
        }
    }
}