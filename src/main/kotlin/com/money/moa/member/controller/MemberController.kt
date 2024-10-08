package com.money.moa.member.controller

import com.money.moa.common.dto.ResponseDto
import com.money.moa.member.dto.request.MemberLoginRequest
import com.money.moa.member.dto.request.MemberSaveRequest
import com.money.moa.member.service.MemberService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(value = ["/api/v1/member"])
class MemberController(
        private val memberService: MemberService
) {
    @PostMapping("/login")
    fun login(response: HttpServletResponse, @RequestBody memberLoginRequest: MemberLoginRequest) {
        memberService.login(response, memberLoginRequest)
    }

    @PostMapping("/save")
    fun join(@RequestBody memberSaveRequest: MemberSaveRequest): ResponseEntity<out ResponseDto<out Any?>> {
        return memberService.saveMember(memberSaveRequest)
    }

    @PostMapping("/logout")
    fun logout(httpServletRequest: HttpServletRequest): ResponseEntity<ResponseDto<*>> {
        return memberService.logout(httpServletRequest)
    }
}