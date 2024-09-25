package com.common.resolver

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.apache.commons.lang3.StringUtils
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver
import java.nio.charset.CharsetEncoder
import java.nio.charset.StandardCharsets

class CommonHandlerExceptionResolver : AbstractHandlerExceptionResolver() {
    override fun doResolveException(request: HttpServletRequest, response: HttpServletResponse, handler: Any?, ex: java.lang.Exception): ModelAndView? {
        val exceptionClassName = ex.javaClass.getName()
        if (StringUtils.contains(exceptionClassName, "org.springframework.security.core.userdetails.UsernameNotFoundException")) {
            response.status = HttpStatus.UNAUTHORIZED.value()
        } else if (StringUtils.contains(exceptionClassName, "org.springframework.http.converter.HttpMessageNotReadableException")) {
            response.status = HttpStatus.BAD_REQUEST.value()
        } else {
            response.status = HttpStatus.INTERNAL_SERVER_ERROR.value()
        }
        response.contentType = MediaType.APPLICATION_JSON.type
        response.characterEncoding = StandardCharsets.UTF_8.name()

        val objectMapper = ObjectMapper()
        try {
            response.writer.write(objectMapper.writeValueAsString(ex.message))
            response.writer.flush()
        } catch (e: Exception) {
            logger.error("", e)
        }

        return null
    }
}