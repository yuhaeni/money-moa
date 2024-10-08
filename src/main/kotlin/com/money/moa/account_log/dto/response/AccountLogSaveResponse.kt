package com.money.moa.account_log.dto.response

import java.math.BigInteger
import java.time.LocalDate

class AccountLogSaveResponse(
        var date: LocalDate,
        var money: BigInteger,
        var detail: String,
)