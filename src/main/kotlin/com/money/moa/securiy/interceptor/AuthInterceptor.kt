package com.money.moa.securiy.interceptor

import com.money.moa.securiy.CustomUserDetails
import com.money.moa.securiy.jwt.JwtProvider
import io.jsonwebtoken.ExpiredJwtException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.HandlerInterceptor
import java.lang.IllegalArgumentException
import java.security.SignatureException

@Configuration
class AuthInterceptor(private val jwtProvider: JwtProvider) : HandlerInterceptor {
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        var encryptAccessToken = jwtProvider.resolveAccessTokenInHeader(request)
        if (encryptAccessToken.isBlank()) {
            return false
        }

        encryptAccessToken = encryptAccessToken.replace("Bearer ", "")
        val accessToken = jwtProvider.decryptToken(encryptAccessToken)
        try {
            val isValidateToken = jwtProvider.validateToken(accessToken)
            if (!isValidateToken) {
                jwtProvider.removeAuthentication(request, response)
                return false
            }
            val authentication = jwtProvider.getAuthentication(accessToken)
            val memberDetails = authentication.principal as CustomUserDetails

            if (jwtProvider.isBlackListToken(memberDetails.userName)) {
                return false
            }

            request.setAttribute("_memberDetails", memberDetails)
        } catch (e: ExpiredJwtException) {
            val encryptRefreshToken = jwtProvider.resolveRefreshTokenInRedis(e.claims.subject)
            val refreshToken = jwtProvider.decryptToken(encryptRefreshToken)
            val isValidateToken = jwtProvider.validateToken(refreshToken)
            if (!isValidateToken) {
                jwtProvider.destroyToken(request, response, e.claims.subject)
                return false
            }

            jwtProvider.issueToken(response, e.claims)
            return true
        } catch (e: SignatureException) {
            jwtProvider.removeAuthentication(request, response)

            return false
        } catch (e: IllegalArgumentException) {
            jwtProvider.removeAuthentication(request, response)

            return false
        } catch (e: Exception) {
            jwtProvider.removeAuthentication(request, response)
            return false
        }

        return true
    }

}