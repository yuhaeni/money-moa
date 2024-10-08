package com.money.moa.common.exception.handler

import com.money.moa.common.dto.ResponseDto
import com.money.moa.common.exception.CommonException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class CommonExceptionHandler {
    @ExceptionHandler(CommonException::class)
    fun handleBizException(ex: CommonException): ResponseEntity<ResponseDto<*>> {
        val response = ResponseDto(
                code = ex.code ?: HttpStatus.INTERNAL_SERVER_ERROR.value(),
                message = ex.message,
                error = ex.message,
                response = null // 일단 null
        )
        return ResponseEntity.status(response.code).body(response)
    }
}