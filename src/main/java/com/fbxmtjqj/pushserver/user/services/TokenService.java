package com.fbxmtjqj.pushserver.user.services;

import com.fbxmtjqj.pushserver.common.jwt.JwtHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final JwtHandler jwtHandler;

    @Value("${jwt.max-age.access}")
    private long accessTokenMaxAgeSeconds;

    @Value("${jwt.max-age.refresh}")
    private long refreshTokenMaxAgeSeconds;

    @Value("${jwt.key.access}")
    private String accessKey;

    @Value("${jwt.key.refresh}")
    private String refreshKey;

    public String createToken(final String tokenType, final String userId) {
        if (tokenType.equals("access")) {
            return jwtHandler.createToken(accessKey, userId, accessTokenMaxAgeSeconds);
        }

        if (tokenType.equals("refresh")) {
            return jwtHandler.createToken(refreshKey, userId, refreshTokenMaxAgeSeconds);
        }

        return null;
    }
}
