package com.money.moa.redis.util

import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class RedisUtil(private val redisTemplate: StringRedisTemplate) {
    fun setRedisValueWithTimeout(key: String, value: String, ttl: Long) {
        this.redisTemplate.opsForValue().set(key, value, ttl, TimeUnit.SECONDS)
    }

    fun getRedisValue(key: String): String {
        return redisTemplate.opsForValue().get(key).orEmpty()
    }

    fun modifyRedisValue(key: String, value: String) {
        val valueOperations = redisTemplate.opsForValue();
        val existingValue = valueOperations.get(key).orEmpty()
        if (existingValue.isNotBlank()) {
            val ttl = redisTemplate.getExpire(existingValue)
            valueOperations.set(key, value, ttl, TimeUnit.SECONDS)
        }
    }

    fun removeRedisValue(key: String) {
        redisTemplate.delete(key)
    }
}