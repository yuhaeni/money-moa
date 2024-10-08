package com.money.moa.redis

import com.money.moa.redis.util.RedisUtil
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class RedisTest @Autowired constructor(private val redisUtil: RedisUtil) {

    @Test
    fun setRedisValueWithTimeout() {
        try {
            redisUtil.setRedisValueWithTimeout("Authorization-Refresh:admin@gmail.com", "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBnbWFpbC5jb20iLCJhdXRob3JpdGllcyI6WyJST0xFX0FETUlOIl0sImlhdCI6MTcyNzA1NDAzNSwiZXhwIjoxNzI3MDU3NjM1fQ.ROY__e0QPk6l0SSRWu_GnlI4XMixgAmK_cuJnb2B_AQ", 180)
        } catch (e: Exception) {
            println(e.toString())
        }
    }
}