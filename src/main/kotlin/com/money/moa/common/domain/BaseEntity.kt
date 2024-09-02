package com.money.moa.common.domain

import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
open class BaseEntity {
    @CreatedDate
    var regDt: LocalDateTime? = LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault())
    @LastModifiedBy
    var modDt: LocalDateTime? = LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault())
}