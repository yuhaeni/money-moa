package com.money.moa.common.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "file")
class FileProperties(
        val uploadDir: String
)