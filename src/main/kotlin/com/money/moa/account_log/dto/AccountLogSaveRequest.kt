package com.money.moa.account_log.dto

import com.money.moa.account_log.domain.AccountLog
import com.money.moa.category.domain.Category
import com.money.moa.member.domain.Member
import java.math.BigInteger
import java.time.LocalDate

class AccountLogSaveRequest(
        var date: LocalDate,
        var money: BigInteger,
        var detail: String,
        var memberId: Long,
        var categoryId: Long
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