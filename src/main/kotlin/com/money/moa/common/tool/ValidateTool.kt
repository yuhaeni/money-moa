package com.money.moa.common.tool

import java.util.regex.Pattern

class ValidateTool {
    companion object {
        private const val PATTERN_EMAIL: String = "[0-9a-zA-Z]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$"
        private const val PATTERN_PASSWORD: String = "^(?=.*[0-9])(?=.*[$@$!%*#?&.])[[0-9]$@$!%*#?&.]{8,20}$"

        fun isValidEmail(email: String?): Boolean {
            if (email.isNullOrBlank()) {
                return false
            }
            return matchesPattern(PATTERN_EMAIL, email)
        }

        fun isValidPassword(password: String?): Boolean {
            if (password.isNullOrBlank()) {
                return false
            }
            return matchesPattern(PATTERN_PASSWORD, password)
        }

        private fun matchesPattern(pattern: String, value: String): Boolean {
            return Pattern.matches(pattern, value)
        }
    }
}