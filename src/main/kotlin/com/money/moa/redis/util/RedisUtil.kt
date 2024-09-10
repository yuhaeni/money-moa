package com.money.moa.redis.util

import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.core.ValueOperations
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit
@Component
class RedisUtil(private val redisTemplate: StringRedisTemplate) {

    fun setRedisValueWithTimeout(key: String, value: String, ttl: Long) {
        this.redisTemplate.opsForValue().set(key, value, ttl)
    }

    fun setRedisValueWithTimeout(key: String): String {
        return redisTemplate.opsForValue().get(key).orEmpty()
    }

    fun modifyRedisValue(key: String, value: String) {
        val valueOperations: ValueOperations<String, String> = redisTemplate.opsForValue();
        val existingValue: String = valueOperations.get(key).orEmpty();
        if (existingValue.isNotBlank()) {
            val ttl = redisTemplate.getExpire(key)
            valueOperations.set(key, value, ttl, TimeUnit.SECONDS)
        }
    }

    fun removeRedisValue(key: String) {
        redisTemplate.delete(key)
    }
}