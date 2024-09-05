package com.money.moa.member.service

import com.money.moa.common.manager.MemberManager
import com.money.moa.member.domain.Member
import com.money.moa.member.domain.MemberRepository
import com.money.moa.member.dto.request.MemberFindRequest
import com.money.moa.member.dto.request.MemberSaveRequest
import com.money.moa.member.dto.response.MemberFindResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class MemberService @Autowired constructor(
        private val memberRepository: MemberRepository,
        private val passwordEncoder: PasswordEncoder,
        private val memberManager: MemberManager
) {
    fun findMember(memberFindRequest: MemberFindRequest): MemberFindResponse? {
        val member = memberRepository.findByEmail(memberFindRequest.email)
                ?: throw IllegalStateException("member not found")
        val isMatches = passwordEncoder.matches(memberFindRequest.password, member.password)
        if(!isMatches) {
            return null
        }

        return member.fromEntity()
    }

    fun saveMember(memberSaveRequest: MemberSaveRequest) {
        // TODO 공통 응답 포맷 개발
        checkMemberSaveRequest(memberSaveRequest)

        memberSaveRequest.password = passwordEncoder.encode(memberSaveRequest.password)
        memberRepository.save(memberSaveRequest.toEntity());
    }

    private fun checkMemberSaveRequest(memberSaveRequest: MemberSaveRequest) {
        if (memberManager.checkDuplicateMemberEmail(memberSaveRequest.email)) {

        }
        if (memberManager.checkMemberPassword(memberSaveRequest.password)) {

        }
    }
}