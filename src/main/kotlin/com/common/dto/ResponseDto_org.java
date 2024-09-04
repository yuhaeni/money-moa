package com.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ResponseDto_org<T> {
    private int code;
    private String message;
    private String error;

    private T response;

    public ResponseDto_org(int code) {
        this(code, null, null, null);
    }

    public ResponseDto_org(int code, String message) {
        this(code, message, null, null);
    }

    public ResponseDto_org(int code, String message, String error) {
        this(code, message, error, null);
    }

    public ResponseDto_org(int code, String message, T response) {
        this(code, message, null, response);
    }

    public ResponseDto_org(int code, T response) {
        this(code, null, null, response);
    }

    public static ResponseEntity<ResponseDto_org<?>> ok() {
        return ResponseEntity.ok(new ResponseDto_org<>(HttpStatus.OK.value()));
    }

    public static <T> ResponseEntity<ResponseDto_org<T>> ok(T response) {
        return ResponseEntity.ok(new ResponseDto_org<>(HttpStatus.OK.value(), response));
    }

    public static ResponseEntity<ResponseDto_org<?>> accepted() {
        return ResponseEntity.accepted().body(new ResponseDto_org<>(HttpStatus.ACCEPTED.value()));
    }

    public static <T> ResponseEntity<ResponseDto_org<T>> accepted(T response) {
        return ResponseEntity.accepted().body(new ResponseDto_org<>(HttpStatus.ACCEPTED.value(), response));
    }

    public static ResponseEntity<ResponseDto_org<?>> created() {
        ResponseDto_org<?> body = new ResponseDto_org<>(HttpStatus.OK.value());
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    public static <T> ResponseEntity<ResponseDto_org<T>> created(T response) {
        ResponseDto_org<T> body = new ResponseDto_org<>(HttpStatus.OK.value(), response);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    public static ResponseEntity<ResponseDto_org<?>> badRequest() {
        ResponseDto_org<?> body = new ResponseDto_org<>(HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    public static <T> ResponseEntity<ResponseDto_org<T>> badRequest(T response) {
        ResponseDto_org<T> body = new ResponseDto_org<>(HttpStatus.BAD_REQUEST.value(), response);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    public static ResponseEntity<ResponseDto_org<?>> noContent() {
        ResponseDto_org<?> body = new ResponseDto_org<>(HttpStatus.NO_CONTENT.value(), null, HttpStatus.NO_CONTENT.name());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(body);
    }

    public static <T> ResponseEntity<ResponseDto_org<T>> noContent(T response) {
        ResponseDto_org<T> body = new ResponseDto_org<>(HttpStatus.NO_CONTENT.value(), null, HttpStatus.NO_CONTENT.name(), response);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(body);
    }

    public static ResponseEntity<ResponseDto_org<?>> unprocessableEntity() {
        ResponseDto_org<?> body = new ResponseDto_org<>(HttpStatus.UNPROCESSABLE_ENTITY.value(), null, HttpStatus.UNPROCESSABLE_ENTITY.name());
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(body);
    }

    public static <T> ResponseEntity<ResponseDto_org<T>> unprocessableEntity(T response) {
        return ResponseEntity.unprocessableEntity().body(new ResponseDto_org<>(HttpStatus.UNPROCESSABLE_ENTITY.value(), response));
    }

    public ResponseEntity<ResponseDto_org<?>> responseEntity() {
        return ResponseEntity.status(this.code).body(this);
    }

    public ResponseEntity<ResponseDto_org<?>> responseEntity(HttpStatus httpStatus) {
        return ResponseEntity.status(httpStatus).body(this);
    }
}