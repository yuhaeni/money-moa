package com.money.moa.member.domain

import org.springframework.data.repository.CrudRepository

interface MemberRepository : CrudRepository<Member, Long> {
    fun findByEmail(email: String): Member?
}