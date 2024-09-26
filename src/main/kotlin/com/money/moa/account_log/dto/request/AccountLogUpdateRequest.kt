package com.money.moa.account_log.dto.request

import java.math.BigInteger
import java.time.LocalDate

class AccountLogUpdateRequest(
        var date: LocalDate,
        var money: BigInteger,
        var detail: String,
        var accountLogId: Long
)