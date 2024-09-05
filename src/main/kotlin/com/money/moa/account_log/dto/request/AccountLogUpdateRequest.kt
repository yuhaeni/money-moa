package com.money.moa.account_log.dto.request

import com.money.moa.account_log.domain.AccountLog
import com.money.moa.category.domain.Category
import com.money.moa.member.domain.Member
import java.math.BigInteger
import java.time.LocalDate

class AccountLogUpdateRequest(
        var date: LocalDate,
        var money: BigInteger,
        var detail: String,
        var accountLogId: Long
) {
    fun toEntity(member: Member, category: Category): AccountLog {
        return AccountLog(
                date = date,
                money = money,
                detail = detail,
                member = member,
                category = category
        )
    }
}