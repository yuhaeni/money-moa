package com.money.moa.member.dto.response

class MemberFindResponse(
        var email: String,
        var password: String,
        var name: String,
        var role: String,
        var memberId: Long?
)