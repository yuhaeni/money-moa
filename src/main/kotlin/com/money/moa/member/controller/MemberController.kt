package com.money.moa.member.controller

import com.money.moa.member.dto.request.MemberSaveRequest
import com.money.moa.member.service.MemberService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/api/v1/member"])
class MemberController(
        private val memberService: MemberService
) {
    @PostMapping
    fun saveMember(@RequestBody memberSaveRequest: MemberSaveRequest) {
        memberService.saveMember(memberSaveRequest)
    }
}