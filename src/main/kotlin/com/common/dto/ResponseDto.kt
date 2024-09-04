package com.common.dto

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity


data class ResponseDto<T>(
    var code: Int,
    var message: String? = null,
    var error: String? = null,
    var response: T? = null
) {

    constructor(code: Int) : this(code, null, null, null)

    constructor(code: Int, message: String?) : this(code, message, null, null)

    constructor(code: Int, message: String?, error: String?) : this(code, message, error, null)

    constructor(code: Int, message: String?, response: T) : this(code, message, null, response)

    constructor(code: Int, response: T) : this(code, null, null, response)

    fun responseEntity(): ResponseEntity<ResponseDto<*>> {
        return ResponseEntity.status(this.code).body(this)
    }

    fun responseEntity(httpStatus: HttpStatus?): ResponseEntity<ResponseDto<*>> {
        return ResponseEntity.status(httpStatus!!).body(this)
    }

    companion object {
        fun ok(): ResponseEntity<ResponseDto<*>> {
            return ResponseEntity.ok(ResponseDto<Any>(HttpStatus.OK.value()))
        }

        fun <T> ok(response: T): ResponseEntity<ResponseDto<T>> {
            return ResponseEntity.ok(ResponseDto(HttpStatus.OK.value(), response))
        }

        fun accepted(): ResponseEntity<ResponseDto<*>> {
            return ResponseEntity.accepted().body(ResponseDto<Any>(HttpStatus.ACCEPTED.value()))
        }

        fun <T> accepted(response: T): ResponseEntity<ResponseDto<T>> {
            return ResponseEntity.accepted()
                .body(ResponseDto(HttpStatus.ACCEPTED.value(), response))
        }

        fun created(): ResponseEntity<ResponseDto<*>> {
            val body: ResponseDto<*> = ResponseDto<Any>(HttpStatus.OK.value())
            return ResponseEntity.status(HttpStatus.CREATED).body(body)
        }

        fun <T> created(response: T): ResponseEntity<ResponseDto<T>> {
            val body = ResponseDto(HttpStatus.OK.value(), response)
            return ResponseEntity.status(HttpStatus.CREATED).body(body)
        }

        fun badRequest(): ResponseEntity<ResponseDto<*>> {
            val body: ResponseDto<*> = ResponseDto<Any>(HttpStatus.BAD_REQUEST.value())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body)
        }

        fun <T> badRequest(response: T): ResponseEntity<ResponseDto<T>> {
            val body = ResponseDto(HttpStatus.BAD_REQUEST.value(), response)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body)
        }

        fun noContent(): ResponseEntity<ResponseDto<*>> {
            val body: ResponseDto<*> =
                ResponseDto<Any>(HttpStatus.NO_CONTENT.value(), null, HttpStatus.NO_CONTENT.name)
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(body)
        }

        fun <T> noContent(response: T): ResponseEntity<ResponseDto<T>> {
            val body: ResponseDto<T> = ResponseDto<T>(
                HttpStatus.NO_CONTENT.value(),
                null,
                HttpStatus.NO_CONTENT.name,
                response
            )
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(body)
        }

        fun unprocessableEntity(): ResponseEntity<ResponseDto<*>> {
            val body: ResponseDto<*> = ResponseDto<Any>(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                null,
                HttpStatus.UNPROCESSABLE_ENTITY.name
            )
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(body)
        }

        fun <T> unprocessableEntity(response: T): ResponseEntity<ResponseDto<T>> {
            return ResponseEntity.unprocessableEntity()
                .body(ResponseDto(HttpStatus.UNPROCESSABLE_ENTITY.value(), response))
        }
    }
}