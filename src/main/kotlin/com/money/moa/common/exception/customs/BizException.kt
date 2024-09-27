package com.money.moa.common.exception.customs

import org.springframework.http.HttpStatus

class BizException : RuntimeException {
    val code: Int?
    val data: Array<out Any>?

    // 기본 생성자
    constructor() : super() {
        this.code = null
        this.data = null
    }

    // 메시지만 있는 생성자
    constructor(message: String) : super(message) {
        this.code = null
        this.data = null
    }

    // 코드와 메시지가 있는 생성자
    constructor(code: Int, message: String) : super(message) {
        this.code = code
        this.data = null
    }

    // HttpStatus 코드와 메세지가 있는 생성자
    constructor(code: HttpStatus, message: String) : super(message) {
        this.code = code.value()
        this.data = null
    }

    // 코드, 메시지, 추가 데이터가 있는 생성자
    constructor(code: Int, message: String, vararg data: Any) : super(message) {
        this.code = code
        this.data = data
    }

    // 메시지와 추가 데이터가 있는 생성자
    constructor(message: String, vararg data: Any) : super(message) {
        this.code = null
        this.data = data
    }

    // 원인 예외를 포함한 생성자
    constructor(cause: Throwable) : super(cause) {
        this.code = null
        this.data = null
    }

    // 메시지와 원인 예외를 포함한 생성자
    constructor(message: String, cause: Throwable) : super(message, cause) {
        this.code = null
        this.data = null
    }

    // 코드, 메시지, 원인 예외를 포함한 생성자
    constructor(code: Int, message: String, cause: Throwable) : super(message, cause) {
        this.code = code
        this.data = null
    }
}
