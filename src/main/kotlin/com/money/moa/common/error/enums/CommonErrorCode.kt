package com.money.moa.common.error.enums

import com.money.moa.common.error.enums.`interface`.ErrorCodeEnum
import org.springframework.http.HttpStatus

enum class CommonErrorCode(val httpStatus: HttpStatus, val message: String) : ErrorCodeEnum {
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "Invalid parameter"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"),
    REQUIRED_PARAMETER(HttpStatus.BAD_REQUEST, "A Required Parameter Is Missing."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "not found");

    override fun httpStatus(): HttpStatus {
        return httpStatus
    }

    override fun message(): String {
        return message
    }
}