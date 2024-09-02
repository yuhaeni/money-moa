package com.money.moa.member.domain

import com.money.moa.account_log.domain.AccountLog
import com.money.moa.common.domain.BaseEntity
import jakarta.persistence.*
import lombok.Getter

@Entity
@Table
@Getter
class Member(
        @Column
        var email: String,

        @Column
        var name: String,

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val memberId: Long? = null
) : BaseEntity()