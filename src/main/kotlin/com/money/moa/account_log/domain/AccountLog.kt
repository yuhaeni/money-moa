package com.money.moa.account_log.domain

import com.money.moa.category.domain.Category
import com.money.moa.common.domain.BaseEntity
import com.money.moa.member.domain.Member
import jakarta.persistence.*
import lombok.Getter
import java.math.BigInteger
import java.time.LocalDate

@Table
@Entity
@Getter
class AccountLog(
        @Column
        var date: LocalDate,

        @Column
        var money: BigInteger,

        @Column
        var detail: String,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "member_id")
        var member: Member,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "category_id")
        var category: Category,

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var accountLogId: Long? = null
) : BaseEntity()