package com.money.moa.member.dto.response

import com.money.moa.common.enums.Role

class MemberFindResponse(
        var email: String,
        var password: String,
        var name: String,
        var role: Role,
        var memberId: Long?
)