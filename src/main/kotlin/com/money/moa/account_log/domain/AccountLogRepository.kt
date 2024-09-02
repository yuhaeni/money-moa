package com.money.moa.account_log.domain

import com.money.moa.member.domain.Member
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface AccountLogRepository : CrudRepository<AccountLog, Long> {
    @Query("select a from AccountLog a join fetch a.member join fetch a.category")
    fun findAccountLog(member: Member): ArrayList<AccountLog>
}