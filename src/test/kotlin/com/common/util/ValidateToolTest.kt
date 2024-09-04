package com.common.util

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ValidateToolTest {

    @Test
    fun runTests() {
        val tests = listOf(
            Pair("Test isValidPattern with Valid Pattern") { assertTrue(ValidateTools.isValidPattern("Hello123"), "영문자와 숫자로만 구성된 문자열은 유효함") },
            Pair("Test isValidPattern with Spaces") { assertFalse(ValidateTools.isValidPattern("Hello 123"), "공백이 포함된 문자열은 유효하지 않음") },
            Pair("Test isValidPattern with Special Characters") { assertFalse(ValidateTools.isValidPattern("Hello@123"), "특수 문자가 포함된 문자열은 유효하지 않음") },
            Pair("Test isValidPattern with Non-English Characters") { assertFalse(ValidateTools.isValidPattern("안녕하세요123"), "한글이 포함된 문자열은 유효하지 않음") },
            Pair("Test isValidPattern with Null Input") { assertFalse(ValidateTools.isValidPattern(null), "null 입력은 유효하지 않음") },
            Pair("Test isValidPattern with Empty String") { assertFalse(ValidateTools.isValidPattern(""), "빈 문자열은 유효하지 않음") }
        )

        for ((testName, test) in tests) {
            try {
                test()
                println("$testName: PASSED")
            } catch (e: AssertionError) {
                println("$testName: FAILED - ${e.message}")
            }
        }
    }
}