package com.money.moa.member.service

import com.money.moa.member.domain.MemberRepository
import com.money.moa.member.dto.MemberSaveRequest
import org.springframework.stereotype.Service

@Service
class MemberService(
        private val memberRepository: MemberRepository
) {
    fun saveMember(memberSaveRequest: MemberSaveRequest) {
        memberRepository.save(memberSaveRequest.toEntity());
    }
}