package com.fbxmtjqj.pushserver.token.service;

import com.fbxmtjqj.pushserver.common.exception.ErrorCode;
import com.fbxmtjqj.pushserver.common.exception.ServerException;
import com.fbxmtjqj.pushserver.common.jwt.JwtService;
import com.fbxmtjqj.pushserver.token.model.dto.RefreshTokenResponse;
import com.fbxmtjqj.pushserver.user.model.entity.User;
import com.fbxmtjqj.pushserver.user.model.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class TokenServiceTest {
    @InjectMocks
    private TokenService target;
    @Mock
    private UserRepository userRepository;
    @Mock
    private JwtService jwtService;

    @Test
    @DisplayName("accessToken 재발급 성공")
    public void successRefreshAccessToken() {
        doReturn(true).when(jwtService).isValidRefreshToken("refreshToken");
        doReturn((long) 1).when(jwtService).getId("refreshToken", false);
        doReturn(Optional.of(getUser())).when(userRepository).findById((long) 1);
        doReturn("accessToken").when(jwtService).createAccessToken(any(User.class));

        final RefreshTokenResponse result = target.refreshAccessToken("refreshToken");

        assertThat(result.getAccessToken()).isEqualTo("accessToken");
    }

    @Test
    @DisplayName("accessToken 재발급 실패 - Invalid Token")
    public void failRefreshAccessTokenInvalidToken() {

        final ServerException result = assertThrows(ServerException.class, () -> target.refreshAccessToken("invalidToken"));

        assertThat(result.getErrorResult()).isEqualTo(ErrorCode.INVALID_TOKEN);
    }

    @Test
    @DisplayName("accessToken 재발급 실패 - 유저조회실패")
    public void failRefreshAccessTokenFindUserFail() {
        doReturn(true).when(jwtService).isValidRefreshToken("refreshToken");
        doReturn((long) 1).when(jwtService).getId("refreshToken", false);
        doReturn(Optional.empty()).when(userRepository).findById((long) 1);

        final ServerException result = assertThrows(ServerException.class, () -> target.refreshAccessToken("refreshToken"));

        assertThat(result.getErrorResult()).isEqualTo(ErrorCode.USER_NOT_FOUND);
    }

    private User getUser() {
        return User.builder()
                .userId("userId")
                .password("password")
                .build();
    }
}
