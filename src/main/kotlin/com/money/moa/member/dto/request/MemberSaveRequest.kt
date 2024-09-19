package com.money.moa.member.dto.request

import com.money.moa.common.enums.Role
import com.money.moa.member.domain.Member

class MemberSaveRequest(
        var email: String,
        var name: String,
        var password: String,
        var role: String
) {
    fun toEntity(): Member {
        return Member(
                email = email,
                name = name,
                password = password,
                role = role
        )
    }
}