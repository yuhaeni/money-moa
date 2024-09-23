package com.money.moa.account_log.service

import com.money.moa.account_log.domain.AccountLog
import com.money.moa.account_log.domain.AccountLogRepository
import com.money.moa.account_log.dto.request.AccountLogFindRequest
import com.money.moa.account_log.dto.request.AccountLogSaveRequest
import com.money.moa.account_log.dto.request.AccountLogUpdateRequest
import com.money.moa.account_log.dto.response.AccountLogFindResponse
import com.money.moa.category.domain.Category
import com.money.moa.category.domain.CategoryRepository
import com.money.moa.member.domain.Member
import com.money.moa.member.domain.MemberRepository
import com.money.moa.securiy.CustomUserDetails
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AccountLogService @Autowired constructor(
        private val accountLogRepository: AccountLogRepository,
        private val memberRepository: MemberRepository,
        private val categoryRepository: CategoryRepository
) {
    fun saveAccountLog(httpServletRequest: HttpServletRequest, accountLogSaveRequest: AccountLogSaveRequest) {
        val member: Member = getMember(httpServletRequest)
        val category: Category = categoryRepository.findByIdOrNull(accountLogSaveRequest.categoryId)
                ?: throw IllegalStateException("category not found")
        accountLogRepository.save(accountLogSaveRequest.toEntity(member, category))
    }

    fun findAccountLog(httpServletRequest: HttpServletRequest, accountLogFindRequest: AccountLogFindRequest): List<AccountLogFindResponse> {
        val member: Member = getMember(httpServletRequest)
        val accountLogList: ArrayList<AccountLog> = accountLogRepository.findAccountLog(member)
        return accountLogList.map { it.fromEntity() }.toList()
    }

    @Transactional
    fun updateAccountLog(httpServletRequest: HttpServletRequest, accountLogUpdateRequest: AccountLogUpdateRequest) {
        // TODO 유효성 검증
        val accountLog: AccountLog = accountLogRepository.findByIdOrNull(accountLogUpdateRequest.accountLogId)
                ?: throw IllegalStateException("account log not found")
        accountLog.updateAccountLog(accountLogUpdateRequest)
    }

    private fun getMember(httpServletRequest: HttpServletRequest): Member {
        val customUserDetails = httpServletRequest.getAttribute("_memberDetails") as CustomUserDetails

        return memberRepository.findByEmail(customUserDetails.userName)
                ?: throw IllegalStateException("member not found")
    }
}