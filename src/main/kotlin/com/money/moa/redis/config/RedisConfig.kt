package com.money.moa.redis.config

import org.springframework.boot.autoconfigure.data.redis.RedisProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisPassword
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.StringRedisTemplate

@Configuration
class RedisConfig(private val redisProperties: RedisProperties) {

    @Bean
    fun redisConnectionFactory(): RedisConnectionFactory {
        val redisConfig = RedisStandaloneConfiguration()
        redisConfig.port = redisProperties.port
        redisConfig.hostName = redisProperties.host
        redisConfig.password = RedisPassword.of(redisProperties.password)

        return LettuceConnectionFactory(redisConfig)
    }

    @Bean
    fun redisTemplate(): StringRedisTemplate {
        return StringRedisTemplate(redisConnectionFactory())
    }
}