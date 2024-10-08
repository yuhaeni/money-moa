package com.money.moa.member.domain

import com.money.moa.common.domain.BaseEntity
import com.money.moa.member.dto.response.MemberFindResponse
import com.money.moa.member.dto.response.MemberSaveResponse
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

//        @Enumerated(EnumType.STRING)
//        var role: Role = Role.ADMIN,
        @Column
        var role: String,

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val memberId: Long? = null
) : BaseEntity() {
    fun fromEntity(): MemberFindResponse {
        return MemberFindResponse(
                email = email, password = password, name = name, role = role, memberId = memberId
        )
    }

    fun toDto(): MemberSaveResponse {
        return MemberSaveResponse(
            email = this.email,
            name = this.name,
            role = this.role
        )
    }

}

