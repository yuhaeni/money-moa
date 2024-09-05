package com.money.moa.securiy

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
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
@EnableMethodSecurity(securedEnabled = true)
class SecurityConfig {
    // 출처: https://growingsaja.tistory.com/1025
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
                .csrf { csrfConfig: CsrfConfigurer<HttpSecurity> ->
                    csrfConfig.disable()
                }
                .formLogin { formLoginConfig ->
                    formLoginConfig.disable()
                }
                .httpBasic { httpBasicConfig -> httpBasicConfig.disable() }
                .headers { headerConfig: HeadersConfigurer<HttpSecurity> ->
                    headerConfig.frameOptions { frameOptionsConfig -> frameOptionsConfig.disable() }
                }
                .authorizeHttpRequests { authorizeHttpRequests ->
                    authorizeHttpRequests
                            .requestMatchers("/api/v1/member/**").permitAll()
                            .requestMatchers("/api/v1/category/**").permitAll()
                }

        return http.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder()
    }
}