package com.money.moa

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class MoaApplication

fun main(args: Array<String>) {
    runApplication<MoaApplication>(*args)
}
