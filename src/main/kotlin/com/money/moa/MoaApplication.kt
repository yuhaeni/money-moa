package com.money.moa

import com.money.moa.securiy.jwt.properties.JwtProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
@EnableConfigurationProperties(JwtProperties::class)
@ConfigurationPropertiesScan
class MoaApplication

fun main(args: Array<String>) {
    println("hi kotmul")
    println("hellow kongmoon")
    runApplication<MoaApplication>(*args)
}
