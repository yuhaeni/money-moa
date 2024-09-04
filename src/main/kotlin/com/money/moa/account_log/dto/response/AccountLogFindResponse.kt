package com.money.moa.account_log.dto.response
import java.math.BigInteger
import java.time.LocalDate

class AccountLogFindResponse(
        var date: LocalDate,
        var money: BigInteger,
        var detail: String,
        var categoryName: String,
        var categoryType: Char
)