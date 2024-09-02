package com.money.moa.account_log.service

import com.money.moa.account_log.domain.AccountLog
import com.money.moa.account_log.domain.AccountLogRepository
import com.money.moa.account_log.dto.AccountLogFindRequest
import com.money.moa.account_log.dto.AccountLogSaveRequest
import com.money.moa.category.domain.Category
import com.money.moa.category.domain.CategoryRepository
import com.money.moa.member.domain.Member
import com.money.moa.member.domain.MemberRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class AccountLogService @Autowired constructor(
        private val accountLogRepository: AccountLogRepository,
        private val memberRepository: MemberRepository,
        private val categoryRepository: CategoryRepository
) {
    fun saveAccountLog(accountLogSaveRequest: AccountLogSaveRequest) {
        val member: Member = memberRepository.findByIdOrNull(accountLogSaveRequest.memberId) ?: throw IllegalStateException("member not found")
        val category: Category = categoryRepository.findByIdOrNull(accountLogSaveRequest.categoryId) ?: throw IllegalStateException("category not found")
        accountLogRepository.save(accountLogSaveRequest.toEntity(member, category))
    }

    fun findAccountLog(accountLogFindRequest: AccountLogFindRequest): ArrayList<AccountLog> {
        val member = memberRepository.findByIdOrNull(accountLogFindRequest.memberId) ?: throw IllegalStateException("member not found")
        return accountLogRepository.findAccountLogByMember(member)
    }
}