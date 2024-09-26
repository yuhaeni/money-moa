package com.money.moa.member.service

import com.common.dto.ResponseDto
import com.money.moa.common.manager.MemberManager
import com.money.moa.member.domain.MemberRepository
import com.money.moa.member.dto.request.MemberFindRequest
import com.money.moa.member.dto.request.MemberSaveRequest
import com.money.moa.member.dto.response.MemberFindResponse
import com.money.moa.securiy.jwt.JwtProvider
import com.money.moa.securiy.user.CustomUserDetailsService
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

    fun saveMember(memberSaveRequest: MemberSaveRequest): ResponseEntity<out ResponseDto<out Any?>> {
        if (!memberManager.checkDuplicateMemberEmail(memberSaveRequest.email)) {
            return ResponseDto.badRequest("이미 존재하는 이메일입니다.")
        }
        if(!memberManager.checkMemberPassword(memberSaveRequest.password)){
            return ResponseDto.badRequest("유효하지 않은 비밀번호 입니다.")
        }

        memberSaveRequest.password = passwordEncoder.encode(memberSaveRequest.password)
        memberRepository.save(memberSaveRequest.toEntity());
        return ResponseDto.ok()
    }
}