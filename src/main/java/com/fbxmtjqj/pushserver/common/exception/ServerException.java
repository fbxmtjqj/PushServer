package com.fbxmtjqj.pushserver.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ServerException extends RuntimeException {

    private final ErrorCode errorResult;

}