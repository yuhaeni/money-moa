package com.money.moa.common.exception.handler

import com.money.moa.common.dto.ResponseDto
import com.money.moa.common.exception.CommonException
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
class CommonExceptionHandler {
    // BizException 발생 시 handler를 통해 리턴 코드 변경 (테스트 필요) CommonExceptionHandler_org.java 참고

    @ExceptionHandler(CommonException::class)
    fun handleBizException(ex: CommonException): ResponseEntity<ResponseDto<*>> {
        val response = ResponseDto(
                code = ex.code ?: HttpStatus.INTERNAL_SERVER_ERROR.value(),
                message = ex.message,
                error = ex.message, // TODO message와 같아서 어떤 데이터를 보여주는 게 좋을지 논의 필요
                response = null // 일단 null
        )
        return ResponseEntity.status(response.code).body(response)
    }
}