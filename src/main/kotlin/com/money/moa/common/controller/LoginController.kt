package com.money.moa.common.controller

import com.common.dto.ResponseDto
import com.money.moa.member.dto.request.MemberFindRequest
import com.money.moa.member.dto.request.MemberSaveRequest
import com.money.moa.member.dto.response.MemberResponse
import com.money.moa.member.service.MemberService
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseEntity
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
    fun join(@RequestBody memberSaveRequest: MemberSaveRequest): ResponseEntity<out ResponseDto<out Any?>> {
        return memberService.saveMember(memberSaveRequest)
    }

    @PostMapping("/save")
    fun save(@RequestBody memberSaveRequest: MemberSaveRequest): ResponseEntity<ResponseDto<MemberResponse>> {
        return memberService.saveMember(memberSaveRequest)
    }
}