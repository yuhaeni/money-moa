package com.money.moa.common.manager

import com.money.moa.common.tool.ValidateTool
import com.money.moa.member.domain.Member
import com.money.moa.member.domain.MemberRepository
import org.springframework.stereotype.Component

@Component
class MemberManager(
        private val memberRepository: MemberRepository,
) {

    fun checkDuplicateMemberEmail(email: String?): Boolean {
        if (checkMemberEmail(email)) {
            return false
        }

        val member: Member? = email?.let { memberRepository.findByEmail(it) }
        return member != null
    }

    fun checkMemberEmail(email: String?): Boolean {
        if (
                email.isNullOrBlank()
                || checkValidMemberEmail(email)
        ) {
            return false
        }

        return true
    }

    fun checkValidMemberEmail(email: String?): Boolean {
        return ValidateTool.isValidEmail(email)
    }

    fun checkMemberPassword(password: String?): Boolean {
        return !(password.isNullOrBlank()
                || checkValidMemberPassword(password))
    }

    private fun checkValidMemberPassword(password: String?): Boolean {
        return ValidateTool.isValidPassword(password)
    }

}