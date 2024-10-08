package com.money.moa.common.error.enums.`interface`

import org.springframework.http.HttpStatus

interface ErrorCodeEnum {
    fun httpStatus(): HttpStatus
    fun message(): String
}