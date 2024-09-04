package com.common.util

// top-level constants
const val VALID_PATTERN_REGEX = "^[a-zA-Z0-9]+$"

// top-level function
val validPatternRegex = VALID_PATTERN_REGEX.toRegex()

// object
object ValidateTools {

    fun isValidPattern(input: String?): Boolean {
        if (input.isNullOrEmpty()) return false

        val regex = "^[a-zA-Z0-9]+$".toRegex()
        return regex.matches(input)
    }

}