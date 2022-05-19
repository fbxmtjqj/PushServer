package com.fbxmtjqj.pushserver.token.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class RefreshTokenResponse {

    final String accessToken;
}
