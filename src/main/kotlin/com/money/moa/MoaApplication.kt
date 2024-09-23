package com.money.moa

import com.common.util.ValidateTools
import com.common.util.validPatternRegex
import com.money.moa.securiy.jwt.properties.JwtProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
@EnableConfigurationProperties(JwtProperties::class)
@ConfigurationPropertiesScan
class MoaApplication

fun main(args: Array<String>) {
    runApplication<MoaApplication>(*args)

    val a = "abc"
    val matches = validPatternRegex.matches(a)
    val validPattern = ValidateTools.isValidPattern(a)
    if (matches == validPattern)
        println("hi kotmul")
}
