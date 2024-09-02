package com.money.moa.account_log.domain

import com.money.moa.account_log.dto.AccountLogFindRequest
import com.money.moa.member.domain.Member
import org.springframework.data.repository.CrudRepository

interface AccountLogRepository : CrudRepository<AccountLog, Long> {
    fun findAccountLogByMember(member: Member): ArrayList<AccountLog>
}