package com.money.moa.common.exception.resolver

import com.money.moa.common.exception.enums.ExceptionEnum
import com.fasterxml.jackson.databind.ObjectMapper
import com.money.moa.common.dto.ResponseDto
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver
import java.nio.charset.StandardCharsets

class CommonHandlerExceptionResolver : AbstractHandlerExceptionResolver() {
    override fun doResolveException(request: HttpServletRequest, response: HttpServletResponse, handler: Any?, ex: java.lang.Exception): ModelAndView? {
        var view = ModelAndView()

        val exceptionClassName = ex.javaClass.getName()
        val exceptionClass = ExceptionEnum.ExceptionClassName.findClassName(exceptionClassName)
        if (exceptionClass == null) {
            // TODO null 반환하면 ExceptionHandler가 동작하는데, 올바른 방식으로 구현한 건지??
            return null
        }

        response.status = exceptionClass.httpStatus
        response.contentType = MediaType.APPLICATION_JSON.type
        response.characterEncoding = StandardCharsets.UTF_8.name()

        val objectMapper = ObjectMapper()
        try {
            response.writer.write(objectMapper.writeValueAsString(ResponseDto.badRequest()))
            response.writer.flush()
        } catch (e: Exception) {
            logger.error("", e)
        }

        return view
    }
}