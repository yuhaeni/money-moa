package com.money.moa.account_log.controller

import com.common.dto.ResponseDto
import com.money.moa.account_log.domain.AccountLog
import com.money.moa.account_log.dto.request.AccountLogFindRequest
import com.money.moa.account_log.dto.request.AccountLogSaveRequest
import com.money.moa.account_log.dto.request.AccountLogUpdateRequest
import com.money.moa.account_log.dto.response.AccountLogFindResponse
import com.money.moa.account_log.service.AccountLogService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

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
    fun findAccountLog(httpServletRequest: HttpServletRequest, @RequestBody accountLogFindRequest: AccountLogFindRequest): List<AccountLogFindResponse> {
        return accountLogService.findAccountLog(httpServletRequest, accountLogFindRequest)
    }

    @PutMapping
    fun updateAccountLog(httpServletRequest: HttpServletRequest, @RequestBody accountLogUpdateRequest: AccountLogUpdateRequest): ResponseEntity<ResponseDto<*>> {
        return accountLogService.updateAccountLog(httpServletRequest, accountLogUpdateRequest)
    }

}