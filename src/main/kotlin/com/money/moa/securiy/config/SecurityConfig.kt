package com.money.moa.securiy.config

import com.money.moa.common.enums.Role
import com.money.moa.common.util.AES256
import com.money.moa.redis.util.RedisUtil
import com.money.moa.securiy.filter.JwtAuthFilter
import com.money.moa.securiy.interceptor.AuthInterceptor
import com.money.moa.securiy.jwt.JwtProvider
import com.money.moa.securiy.jwt.properties.JwtProperties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
class SecurityConfig(private val jwtProperties: JwtProperties, private val redisTemplate: StringRedisTemplate) {
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
                .anonymous { anonymousConfig -> anonymousConfig.disable() }
                .authorizeHttpRequests { authorizeHttpRequests ->
                    authorizeHttpRequests
                            .requestMatchers("/login", "/join", "/api/v1/account-log/**").permitAll()
                            .requestMatchers(HttpMethod.POST, "/api/v1/category/**").hasAnyAuthority(Role.ADMIN.value())
                            .requestMatchers(HttpMethod.GET, "/api/v1/category/**").permitAll()
                }
                .addFilterBefore(jwtAuthFilter(), UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder()
    }

    @Bean
    fun authInterceptor(jwtProvider: JwtProvider): AuthInterceptor {
        return AuthInterceptor(jwtProvider)
    }

    @Bean
    fun jwtAuthFilter(): JwtAuthFilter {
        return JwtAuthFilter(jwtProvider())
    }

    @Bean
    fun jwtProvider(): JwtProvider {
        return JwtProvider(RedisUtil(redisTemplate), AES256(), jwtProperties)
    }
}