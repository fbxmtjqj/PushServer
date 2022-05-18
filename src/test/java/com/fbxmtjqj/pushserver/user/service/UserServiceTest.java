package com.fbxmtjqj.pushserver.user.service;

import com.fbxmtjqj.pushserver.common.exception.ErrorCode;
import com.fbxmtjqj.pushserver.common.exception.ServerException;
import com.fbxmtjqj.pushserver.common.jwt.JwtService;
import com.fbxmtjqj.pushserver.user.model.dto.AddUserResponse;
import com.fbxmtjqj.pushserver.user.model.dto.SignInResponse;
import com.fbxmtjqj.pushserver.user.model.dto.UpdateUserType;
import com.fbxmtjqj.pushserver.user.model.entity.User;
import com.fbxmtjqj.pushserver.user.model.repository.UserRepository;
import com.fbxmtjqj.pushserver.user.services.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    private UserService target;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;

    @Test
    @DisplayName("유저추가성공")
    public void successAddUser() {
        doReturn(getUser()).when(userRepository).save(any(User.class));

        final AddUserResponse result = target.addUser("userId", "password", "test");

        assertThat(result.getHttpStatus()).isEqualTo(HttpStatus.OK);

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("유저signin성공")
    public void successSignIn() {
        doReturn(Optional.of(getUser())).when(userRepository).findByUserId("userId");
        doReturn(true).when(passwordEncoder).matches(anyString(), anyString());

        final SignInResponse result = target.signin("userId", "password");
    }

    @Test
    @DisplayName("유저signin실패_유저조회실패")
    public void failSignInUserNotFound() {
        doReturn(Optional.empty()).when(userRepository).findByUserId("userId");

        final ServerException result = assertThrows(ServerException.class, () -> target.signin("userId", "password"));

        assertThat(result.getErrorResult()).isEqualTo(ErrorCode.USER_NOT_FOUND);

        verify(userRepository, times(1)).findByUserId("userId");
    }

    @Test
    @DisplayName("유저signin실패_비밀번호X")
    public void failSignInPasswordNotEqual() {
        doReturn(Optional.of(getUser())).when(userRepository).findByUserId("userId");

        final ServerException result = assertThrows(ServerException.class, () -> target.signin("userId", "ttt"));

        assertThat(result.getErrorResult()).isEqualTo(ErrorCode.USER_SIGN_IN_FAILED);

        verify(userRepository, times(1)).findByUserId("userId");
    }

    @Test
    @DisplayName("UserType업데이트성공")
    public void successUpdateUserType() {
        doReturn(Optional.of(getUser())).when(userRepository).findByUserId("userId");

        final UpdateUserType result = target.updateUserType("userId", "USER");

        assertThat(result.getHttpStatus()).isEqualTo(HttpStatus.OK);

        verify(userRepository, times(1)).findByUserId("userId");
    }

    @Test
    @DisplayName("UserType업데이트_유저조회실패")
    public void failUpdateUserTypeNotFoundUser() {
        doReturn(Optional.empty()).when(userRepository).findByUserId("userId");

        final ServerException result = assertThrows(ServerException.class, () -> target.updateUserType("userId", "USER"));

        assertThat(result.getErrorResult()).isEqualTo(ErrorCode.USER_NOT_FOUND);

        verify(userRepository, times(1)).findByUserId("userId");
    }

    @Test
    @DisplayName("UserType업데이트_유저타입")
    public void failUpdateUserTypeIllegalArgument() {
        doReturn(Optional.of(getUser())).when(userRepository).findByUserId("userId");

        final ServerException result = assertThrows(ServerException.class, () -> target.updateUserType("userId", "test"));

        assertThat(result.getErrorResult()).isEqualTo(ErrorCode.ILLEGAL_ARGUMENT);

        verify(userRepository, times(1)).findByUserId("userId");
    }

    private User getUser() {
        return getUser("userId", "password", "test");
    }

    private User getUser(final String userId, final String password, final String siteNm) {
        return User.builder()
                .userId(userId)
                .password(password)
                .siteNm(siteNm)
                .build();
    }
}
