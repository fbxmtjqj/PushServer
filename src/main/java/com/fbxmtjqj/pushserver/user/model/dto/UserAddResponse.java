package com.fbxmtjqj.pushserver.user.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@Builder
@RequiredArgsConstructor
public class UserAddResponse {

    private final HttpStatus httpStatus;
}
