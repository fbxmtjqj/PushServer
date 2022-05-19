package com.fbxmtjqj.pushserver.token.service;

import com.fbxmtjqj.pushserver.common.exception.ErrorCode;
import com.fbxmtjqj.pushserver.common.exception.ServerException;
import com.fbxmtjqj.pushserver.common.jwt.JwtService;
import com.fbxmtjqj.pushserver.token.model.dto.RefreshTokenResponse;
import com.fbxmtjqj.pushserver.user.model.entity.User;
import com.fbxmtjqj.pushserver.user.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TokenService {
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public RefreshTokenResponse refreshAccessToken(final String refreshToken) {
        if (!jwtService.isValidRefreshToken(refreshToken)) {
            throw new ServerException(ErrorCode.INVALID_TOKEN);
        }

        final long id = jwtService.getId(refreshToken, false);
        final User user = userRepository.findById(id).orElseThrow(() -> new ServerException(ErrorCode.USER_NOT_FOUND));
        final String accessToken = jwtService.createAccessToken(user);

        return RefreshTokenResponse.builder()
                .accessToken(accessToken)
                .build();
    }
}
