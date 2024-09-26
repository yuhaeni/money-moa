package com.money.moa.account_log.controller

import com.common.dto.ResponseDto
import com.money.moa.account_log.dto.request.AccountLogDeleteRequest
import com.money.moa.account_log.dto.request.AccountLogSaveRequest
import com.money.moa.account_log.dto.request.AccountLogUpdateRequest
import com.money.moa.account_log.dto.response.AccountLogFindResponse
import com.money.moa.account_log.service.AccountLogService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(value = ["/api/v1/account-log"])
class AccountLogController(
        private val accountLogService: AccountLogService
) {
    @PostMapping
    fun saveAccountLog(httpServletRequest: HttpServletRequest, @RequestBody accountLogSaveRequest: AccountLogSaveRequest): ResponseEntity<ResponseDto<*>> {
        return accountLogService.saveAccountLog(httpServletRequest, accountLogSaveRequest)
    }

    @GetMapping
    fun findAccountLog(httpServletRequest: HttpServletRequest): List<AccountLogFindResponse> {
        return accountLogService.findAccountLog(httpServletRequest)
    }

    @PutMapping
    fun updateAccountLog(httpServletRequest: HttpServletRequest, @RequestBody accountLogUpdateRequest: AccountLogUpdateRequest): ResponseEntity<ResponseDto<*>> {
        return accountLogService.updateAccountLog(httpServletRequest, accountLogUpdateRequest)
    }

    @DeleteMapping
    fun deleteAccountLog(httpServletRequest: HttpServletRequest, @RequestBody accountLogDeleteRequest: AccountLogDeleteRequest): ResponseEntity<ResponseDto<*>> {
        return accountLogService.deleteAccountLog(httpServletRequest, accountLogDeleteRequest)
    }

}