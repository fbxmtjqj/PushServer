package com.fbxmtjqj.pushserver.common.model.dto;

import com.fbxmtjqj.pushserver.common.exception.ErrorCode;
import lombok.Builder;

@Builder
public class ErrorResponse {

    private final ErrorCode errorCode;
}
