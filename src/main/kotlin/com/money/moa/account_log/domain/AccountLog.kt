package com.money.moa.account_log.domain

import com.money.moa.account_log.dto.request.AccountLogUpdateRequest
import com.money.moa.account_log.dto.response.AccountLogFindResponse
import com.money.moa.account_log.dto.response.AccountLogSaveResponse
import com.money.moa.category.domain.Category
import com.money.moa.common.domain.BaseEntity
import com.money.moa.member.domain.Member
import jakarta.persistence.*
import lombok.Getter
import org.hibernate.annotations.DynamicUpdate
import java.math.BigInteger
import java.time.LocalDate

@Table
@Entity
@Getter
@DynamicUpdate
class AccountLog(
        @Column
        var date: LocalDate,

        @Column
        var money: BigInteger,

        @Column
        var detail: String,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "memberId")
        var member: Member,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "categoryId")
        var category: Category,

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var accountLogId: Long? = null
) : BaseEntity() {
    fun fromEntity(): AccountLogFindResponse {
        return AccountLogFindResponse(
                date = date,
                money = money,
                detail = detail,
                categoryName = category.categoryName,
                categoryType = category.categoryType
        )
    }

    fun updateAccountLog(accountLogUpdateRequest: AccountLogUpdateRequest) {
        date = accountLogUpdateRequest.date
        money = accountLogUpdateRequest.money
        detail = accountLogUpdateRequest.detail
    }

    fun toDto(): AccountLogSaveResponse {
        return AccountLogSaveResponse(
                date = this.date,
                money = this.money,
                detail = this.detail
        )
    }
}