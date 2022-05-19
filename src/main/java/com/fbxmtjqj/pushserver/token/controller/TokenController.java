package com.fbxmtjqj.pushserver.token.controller;

import com.fbxmtjqj.pushserver.token.model.dto.RefreshTokenResponse;
import com.fbxmtjqj.pushserver.token.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TokenController {
    private final TokenService tokenService;

    @GetMapping("/refresh/accessToken")
    public ResponseEntity<RefreshTokenResponse> refreshAccessToken(
            @RequestHeader("Authorization") final String refreshToken) {

        final RefreshTokenResponse refreshTokenResponse = tokenService.refreshAccessToken(refreshToken);

        return ResponseEntity.status(HttpStatus.OK)
                .body(refreshTokenResponse);
    }
}
