package com.fbxmtjqj.pushserver.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    ILLEGAL_ARGUMENT(HttpStatus.BAD_REQUEST, "부적절한 인수입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User Not found"),
    USER_SIGN_IN_FAILED(HttpStatus.BAD_REQUEST, "User SignIn Failed"),
    DUPLICATED_MEMBERSHIP_REGISTER(HttpStatus.BAD_REQUEST, "Duplicated Membership Register Request"),
    NOT_FOUND_TOKEN(HttpStatus.BAD_REQUEST, "Token Not found"),
    NOT_EQUAL_TOKEN(HttpStatus.BAD_REQUEST, "Token Not equal"),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "Token Invalid"),
    ACCESS_DENIED(HttpStatus.BAD_REQUEST,"ACCESS_DENIED"),
    UNKNOWN_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 오류 발생"),
    ;

    private final HttpStatus httpStatus;
    private final String message;

}