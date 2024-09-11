package com.common.interceptor

import com.money.moa.common.util.AES256
import com.money.moa.securiy.jwt.JwtProvider
import io.jsonwebtoken.ExpiredJwtException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.servlet.HandlerInterceptor

class AuthInterceptor(private val jwtProvider: JwtProvider) : HandlerInterceptor {
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val accessToken = jwtProvider.resolveAccessTokenInHeader(request)
        if (accessToken.isBlank()) {
            return false
        }

        accessToken.replace("Bearer ", "")
        val decryptAccessToken = jwtProvider.decryptToken(accessToken)

        try {
            jwtProvider.validateToken(decryptAccessToken)
            val authentication = jwtProvider.getAuthentication(decryptAccessToken)
            val memberDetails = authentication.principal

            request.setAttribute("_memberDetails", memberDetails)
        } catch (e: ExpiredJwtException) {
//            e.claims.subject
            TODO("refresh token 유효성 검증 후 재발급")
        }

        return true
    }

}