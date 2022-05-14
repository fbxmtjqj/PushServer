package com.fbxmtjqj.pushserver.user.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class SignInResponse {
    private final String accessToken;
    private final String refreshToken;
}
