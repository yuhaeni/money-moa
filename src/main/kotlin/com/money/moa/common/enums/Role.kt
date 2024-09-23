package com.money.moa.common.enums

enum class Role(private var role: String) {
    ADMIN("ROLE_ADMIN")
    , USER("ROLE_USER");

    fun value(): String {
        return role
    }
}