package com.fbxmtjqj.pushserver.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    ILLEGAL_ARGUMENT(HttpStatus.BAD_REQUEST, "ILLEGAL_ARGUMENT"),
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "User Not found"),
    USER_SIGN_IN_FAILED(HttpStatus.BAD_REQUEST, "User SignIn Failed"),
    USER_ALREADY_REGISTER(HttpStatus.BAD_REQUEST, "User already register"),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "Token Invalid"),
    ACCESS_DENIED(HttpStatus.FORBIDDEN,"ACCESS_DENIED"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED"),
    UNKNOWN_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "UNKNOWN_EXCEPTION"),
    NOT_FOUND(HttpStatus.BAD_REQUEST,"Not Found"),

    TEST_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "TEST_ERROR")
    ;

    private final HttpStatus httpStatus;
    private final String message;

}