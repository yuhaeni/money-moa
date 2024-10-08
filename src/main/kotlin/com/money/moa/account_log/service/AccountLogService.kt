package com.money.moa.account_log.service

import com.money.moa.common.dto.ResponseDto
import com.money.moa.account_log.domain.AccountLog
import com.money.moa.account_log.domain.AccountLogRepository
import com.money.moa.account_log.dto.request.AccountLogDeleteRequest
import com.money.moa.account_log.dto.request.AccountLogSaveRequest
import com.money.moa.account_log.dto.request.AccountLogUpdateRequest
import com.money.moa.account_log.dto.response.AccountLogFindResponse
import com.money.moa.account_log.dto.response.AccountLogSaveResponse
import com.money.moa.account_log.manager.AccountLogManager
import com.money.moa.category.domain.Category
import com.money.moa.category.domain.CategoryRepository
import com.money.moa.common.error.enums.CommonErrorCode
import com.money.moa.common.exception.CommonException
import com.money.moa.member.domain.Member
import com.money.moa.member.domain.MemberRepository
import com.money.moa.member.enums.MemberErrorCode
import com.money.moa.securiy.CustomUserDetails
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AccountLogService @Autowired constructor(
        private val accountLogRepository: AccountLogRepository,
        private val memberRepository: MemberRepository,
        private val categoryRepository: CategoryRepository,
        private val accountLogManager: AccountLogManager
) {
    fun saveAccountLog(httpServletRequest: HttpServletRequest, accountLogSaveRequest: AccountLogSaveRequest): ResponseEntity<ResponseDto<AccountLogSaveResponse>> {
        if (accountLogManager.checkBigNumber(accountLogSaveRequest.money)) {
            throw CommonException(CommonErrorCode.INVALID_PARAMETER)
        }

        val member: Member = getMember(httpServletRequest)
        val category: Category? = categoryRepository.findByIdOrNull(accountLogSaveRequest.categoryId)
        if (category == null) {
            throw CommonException(CommonErrorCode.NOT_FOUND)
        }

        val saveAccountLong = accountLogRepository.save(accountLogSaveRequest.toEntity(member, category))
        return ResponseDto.created(saveAccountLong.toDto())
    }

    fun findAccountLog(httpServletRequest: HttpServletRequest): List<AccountLogFindResponse> {
        val member: Member = getMember(httpServletRequest)
        val accountLogList: ArrayList<AccountLog> = accountLogRepository.findAccountLog(member)
        return accountLogList.map { it.fromEntity() }.toList()
    }

    @Transactional
    fun updateAccountLog(httpServletRequest: HttpServletRequest, accountLogUpdateRequest: AccountLogUpdateRequest): ResponseEntity<ResponseDto<*>> {
        if (!accountLogManager.checkBigNumber(accountLogUpdateRequest.money)) {
            throw CommonException(CommonErrorCode.INVALID_PARAMETER)
        }

        val accountLog: AccountLog? = accountLogRepository.findByIdOrNull(accountLogUpdateRequest.accountLogId)
        if (accountLog == null) {
            throw CommonException(CommonErrorCode.NOT_FOUND)
        }

        accountLog.updateAccountLog(accountLogUpdateRequest)
        return ResponseDto.ok()
    }

    fun deleteAccountLog(httpServletRequest: HttpServletRequest, accountLogDeleteRequest: AccountLogDeleteRequest): ResponseEntity<ResponseDto<*>> {
        val accountLog = accountLogRepository.findByIdOrNull(accountLogDeleteRequest.accountLogId)
        if (accountLog == null) {
            throw CommonException(CommonErrorCode.NOT_FOUND)
        }

        val member: Member = getMember(httpServletRequest)
        if (accountLog.member.memberId != member.memberId) {
            throw CommonException(MemberErrorCode.NOT_FOUND_MEMBER)
        }

        accountLogRepository.delete(accountLog)
        return ResponseDto.ok()
    }

    private fun getMember(httpServletRequest: HttpServletRequest): Member {
        val customUserDetails = httpServletRequest.getAttribute("_memberDetails") as CustomUserDetails
        val member = memberRepository.findByEmail(customUserDetails.userName)
        if (member == null) {
            throw CommonException(MemberErrorCode.NOT_FOUND_MEMBER)
        }

        return member
    }
}