package com.money.moa.securiy.jwt.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "jwt")
class JwtProperties(
        val secretKey: String,
        val encryptKey: String,
        val access: Access,
        val refresh: Refresh
) {
    data class Access(
            val tokenHeaderName: String,
            val expireMilliseconds: String,
    )

    data class Refresh(
            val tokenHeaderName: String,
            val expireMilliseconds: String,
    )
}