package com.money.moa.member.dto

import com.money.moa.member.domain.Member

class MemberSaveRequest (
        val email: String,
        val name: String
){
    fun toEntity(): Member {
        return Member(
                email = email,
                name = name
        )
    }
}