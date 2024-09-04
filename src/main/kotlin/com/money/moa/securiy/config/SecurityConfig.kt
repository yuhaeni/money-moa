package com.money.moa.securiy.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.HttpSecurityBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig {
    // 출처: https://growingsaja.tistory.com/1025
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
                .csrf {
                    csrfConfig: CsrfConfigurer<HttpSecurity> -> csrfConfig.disable()
                }
                .headers {headerConfig: HeadersConfigurer<HttpSecurity> ->
                    headerConfig.frameOptions { frameOptionsConfig -> frameOptionsConfig.disable() }
                }
                .authorizeHttpRequests { authorizeHttpRequests ->
                    authorizeHttpRequests
                            .requestMatchers("/api/v1/member/**").permitAll()
                }

        return http.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder()
    }
}