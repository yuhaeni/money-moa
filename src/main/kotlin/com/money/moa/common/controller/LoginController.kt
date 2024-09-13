package com.money.moa.common.controller

import com.money.moa.member.dto.request.MemberFindRequest
import com.money.moa.member.dto.request.MemberSaveRequest
import com.money.moa.member.service.MemberService
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class LoginController(private val memberService: MemberService) {
    @PostMapping("/login")
    fun login(response: HttpServletResponse, @RequestBody memberFindRequest: MemberFindRequest) {
        memberService.login(response, memberFindRequest)
    }

    @PostMapping("/join")
    fun join(@RequestBody memberSaveRequest: MemberSaveRequest) {
        memberService.saveMember(memberSaveRequest)
    }
}