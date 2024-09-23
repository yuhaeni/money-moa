package com.money.moa.securiy.config

import com.money.moa.securiy.interceptor.AuthInterceptor
import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebMvcConfig(private val authInterceptor: AuthInterceptor) : WebMvcConfigurer {
    companion object {
        val ADD_ENDPOINT_LIST: ArrayList<String> = arrayListOf("/api/v1/category", "/api/v1/account-log")
    }

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns(ADD_ENDPOINT_LIST)
                .excludePathPatterns(PathRequest.toStaticResources().atCommonLocations().toString())
    }
}