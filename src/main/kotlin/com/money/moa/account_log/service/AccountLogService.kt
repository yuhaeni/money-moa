package com.money.moa.account_log.service

import com.money.moa.common.dto.ResponseDto
import com.money.moa.account_log.domain.AccountLog
import com.money.moa.account_log.domain.AccountLogRepository
import com.money.moa.account_log.dto.request.AccountLogDeleteRequest
import com.money.moa.account_log.dto.request.AccountLogSaveRequest
import com.money.moa.account_log.dto.request.AccountLogUpdateRequest
import com.money.moa.account_log.dto.response.AccountLogFindResponse
import com.money.moa.account_log.manager.AccountLogManager
import com.money.moa.category.domain.Category
import com.money.moa.category.domain.CategoryRepository
import com.money.moa.common.exception.CommonException
import com.money.moa.member.domain.Member
import com.money.moa.member.domain.MemberRepository
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
    fun saveAccountLog(httpServletRequest: HttpServletRequest, accountLogSaveRequest: AccountLogSaveRequest): ResponseEntity<ResponseDto<*>> {
        if (!accountLogManager.checkBigNumber(accountLogSaveRequest.money)) {
            return ResponseDto.badRequest()
        }

        val member: Member = getMember(httpServletRequest)
        val category: Category = categoryRepository.findByIdOrNull(accountLogSaveRequest.categoryId)
                ?: throw CommonException(HttpStatus.NOT_FOUND, "category not found")

        accountLogRepository.save(accountLogSaveRequest.toEntity(member, category))
        return ResponseDto.ok()
    }

    fun findAccountLog(httpServletRequest: HttpServletRequest): List<AccountLogFindResponse> {
        val member: Member = getMember(httpServletRequest)
        val accountLogList: ArrayList<AccountLog> = accountLogRepository.findAccountLog(member)
        return accountLogList.map { it.fromEntity() }.toList()
    }

    @Transactional
    fun updateAccountLog(httpServletRequest: HttpServletRequest, accountLogUpdateRequest: AccountLogUpdateRequest): ResponseEntity<ResponseDto<*>> {
        if (!accountLogManager.checkBigNumber(accountLogUpdateRequest.money)) {
            throw CommonException(HttpStatus.BAD_REQUEST, "유효하지 않은 금액입니다.")
        }

        val accountLog: AccountLog = accountLogRepository.findByIdOrNull(accountLogUpdateRequest.accountLogId)
                ?: throw CommonException(HttpStatus.NOT_FOUND, "account log not found")
        accountLog.updateAccountLog(accountLogUpdateRequest)
        return ResponseDto.ok()
    }

    fun deleteAccountLog(httpServletRequest: HttpServletRequest, accountLogDeleteRequest: AccountLogDeleteRequest): ResponseEntity<ResponseDto<*>> {
        val accountLog = accountLogRepository.findByIdOrNull(accountLogDeleteRequest.accountLogId)
                ?: throw CommonException(HttpStatus.BAD_REQUEST, "account log not found")

        val member: Member = getMember(httpServletRequest)
        if (accountLog.member.memberId != member.memberId) {
            throw CommonException(HttpStatus.BAD_REQUEST, "member not found")
        }

        accountLogRepository.delete(accountLog)
        return ResponseDto.ok()
    }

    private fun getMember(httpServletRequest: HttpServletRequest): Member {
        val customUserDetails = httpServletRequest.getAttribute("_memberDetails") as CustomUserDetails
        return memberRepository.findByEmail(customUserDetails.userName)
                ?: throw CommonException(HttpStatus.BAD_REQUEST, "member not found")
    }
}