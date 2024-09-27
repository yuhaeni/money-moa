package com.common.exception.customs

import com.common.dto.ResponseDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class CommonExceptionHandler {
    // BizException 발생 시 handler를 통해 리턴 코드 변경 (테스트 필요) CommonExceptionHandler_org.java 참고

    @ExceptionHandler(BizException::class)
    fun handleBizException(ex: BizException, message: String): ResponseEntity<ResponseDto<*>> {
        val response = ResponseDto(
            code = ex.code ?: HttpStatus.INTERNAL_SERVER_ERROR.value(),
            message = ex.message,
            error = message,
            response = null // 일단 null
        )
        return ResponseEntity.status(response.code).body(response)
    }
}