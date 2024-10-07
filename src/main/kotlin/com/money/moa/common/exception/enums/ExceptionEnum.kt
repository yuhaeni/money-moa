package com.money.moa.common.exception.enums

import org.springframework.http.HttpStatus

class ExceptionEnum {
    enum class ExceptionClassName(var className: String, var httpStatus: Int) {
        HTTP_MESSAGE_NOT_READABLE("org.springframework.http.converter.HttpMessageNotReadableException", HttpStatus.BAD_REQUEST.value());

        companion object {
            fun findClassName(className: String): ExceptionClassName? {
                return ExceptionClassName.entries.firstOrNull { it.className == className }
            }
        }
    }
}

