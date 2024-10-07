package com.money.moa.member.enums

import com.money.moa.common.error.enums.`interface`.ErrorCodeEnum
import org.springframework.http.HttpStatus

enum class MemberErrorCode(val httpStatus: HttpStatus, val message: String) : ErrorCodeEnum {
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "Duplicate email"),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "Invalid password"),
    NOT_FOUND_MEMBER(HttpStatus.UNAUTHORIZED, "Not found member");

    override fun httpStatus(): HttpStatus {
        return httpStatus
    }

    override fun message(): String {
        return message
    }
}