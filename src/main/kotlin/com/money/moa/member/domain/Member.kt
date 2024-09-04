package com.money.moa.member.domain

import com.money.moa.account_log.domain.AccountLog
import com.money.moa.common.domain.BaseEntity
import com.money.moa.common.enums.Role
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

        @Column
        var password: String,

        @Enumerated(EnumType.STRING)
        var role: Role = Role.USER,

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val memberId: Long? = null
) : BaseEntity()