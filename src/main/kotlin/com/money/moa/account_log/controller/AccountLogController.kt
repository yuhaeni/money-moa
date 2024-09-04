package com.money.moa.account_log.controller

import com.money.moa.account_log.domain.AccountLog
import com.money.moa.account_log.dto.request.AccountLogFindRequest
import com.money.moa.account_log.dto.request.AccountLogSaveRequest
import com.money.moa.account_log.dto.response.AccountLogFindResponse
import com.money.moa.account_log.service.AccountLogService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/api/v1/account-log"])
class AccountLogController (
        private val accountLogService: AccountLogService
){
    @PostMapping
    fun saveAccountLog(@RequestBody accountLogSaveRequest: AccountLogSaveRequest){
        accountLogService.saveAccountLog(accountLogSaveRequest)
    }

    @GetMapping
    fun findAccountLog(@RequestBody accountLogFindRequest: AccountLogFindRequest): List<AccountLogFindResponse> {
        return accountLogService.findAccountLog(accountLogFindRequest)
    }
}