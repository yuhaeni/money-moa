package com.money.moa.securiy.filter

import com.money.moa.securiy.jwt.JwtProvider
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import lombok.extern.slf4j.Slf4j
import org.springframework.web.filter.OncePerRequestFilter

@Slf4j
class JwtAuthFilter(private val jwtProvider: JwtProvider) : OncePerRequestFilter() {

    private val excludeUrls = listOf("/login", "/join")
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {

        try {
            jwtProvider.filterValidator(request, response)
        } catch (e: Exception) {
            logger.error("{}", e)
            jwtProvider.removeAuthentication(request, response)
        }

        filterChain.doFilter(request, response)
    }

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        return excludeUrls.stream().anyMatch {
            request.servletPath.contains(it)
        }
    }
}