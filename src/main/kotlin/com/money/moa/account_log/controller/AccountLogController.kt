package com.money.moa.account_log.controller

import com.money.moa.account_log.domain.AccountLog
import com.money.moa.account_log.dto.AccountLogFindRequest
import com.money.moa.account_log.dto.AccountLogSaveRequest
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
    fun findAccountLog(@RequestBody accountLogFindRequest: AccountLogFindRequest): ArrayList<AccountLog> {
        return accountLogService.findAccountLog(accountLogFindRequest)
    }
}