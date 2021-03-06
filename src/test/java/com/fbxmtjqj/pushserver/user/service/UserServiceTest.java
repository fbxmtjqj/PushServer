package com.fbxmtjqj.pushserver.user.service;

import com.fbxmtjqj.pushserver.common.exception.ErrorCode;
import com.fbxmtjqj.pushserver.common.exception.ServerException;
import com.fbxmtjqj.pushserver.common.jwt.JwtService;
import com.fbxmtjqj.pushserver.fcm.model.entity.FCM;
import com.fbxmtjqj.pushserver.fcm.model.repository.FCMRepository;
import com.fbxmtjqj.pushserver.user.model.dto.SignInResponse;
import com.fbxmtjqj.pushserver.user.model.entity.User;
import com.fbxmtjqj.pushserver.user.model.repository.GroupRepository;
import com.fbxmtjqj.pushserver.user.model.repository.UserRepository;
import com.fbxmtjqj.pushserver.user.services.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static com.fbxmtjqj.pushserver.common.dto.GroupDTO.getGroup;
import static com.fbxmtjqj.pushserver.common.dto.UserDTO.getUser;
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
    private GroupRepository groupRepository;
    @Mock
    private FCMRepository fcmRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;

    @Test
    @DisplayName("유저추가 성공")
    public void successCreateUser() {
        doReturn(getUser(getGroup())).when(userRepository).save(any(User.class));

        target.createUser("userId", "password", "group");

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("유저추가 실패 - 이미 존재하는 유저")
    public void failCreateUserUserAlreadyRegister() {
        doReturn(Optional.of(getUser())).when(userRepository).findByUserId("userId");

        final ServerException result = assertThrows(ServerException.class, () -> target.createUser("userId", "password", "group"));

        assertThat(result.getErrorResult()).isEqualTo(ErrorCode.USER_ALREADY_REGISTER);
    }

    @Test
    @DisplayName("유저SignIn 성공")
    public void successSignIn() {
        doReturn(Optional.of(getUser())).when(userRepository).findByUserId("userId");
        doReturn(true).when(passwordEncoder).matches(anyString(), anyString());
        doReturn(Optional.of(FCM.builder().token("token").build())).when(fcmRepository).findByToken(anyString());

        final SignInResponse result = target.signIn("userId", "password", "token");

        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("유저SignIn 실패 - 유저 조회 실패")
    public void failSignInUserNotFound() {
        doReturn(Optional.empty()).when(userRepository).findByUserId("userId");

        final ServerException result = assertThrows(ServerException.class, () -> target.signIn("userId", "password", "key"));

        assertThat(result.getErrorResult()).isEqualTo(ErrorCode.USER_NOT_FOUND);

        verify(userRepository, times(1)).findByUserId("userId");
    }

    @Test
    @DisplayName("유저SignIn 실패 - 비밀번호 미일치")
    public void failSignInPasswordNotEqual() {
        doReturn(Optional.of(getUser())).when(userRepository).findByUserId("userId");

        final ServerException result = assertThrows(ServerException.class, () -> target.signIn("userId", "ttt", "key"));

        assertThat(result.getErrorResult()).isEqualTo(ErrorCode.USER_SIGN_IN_FAILED);

        verify(userRepository, times(1)).findByUserId("userId");
    }

    @Test
    @DisplayName("UserType 업데이트 성공")
    public void successModifyUserType() {
        doReturn(Optional.of(getUser())).when(userRepository).findByUserId("userId");

        target.modifyUserType("userId", "USER");

        verify(userRepository, times(1)).findByUserId("userId");
    }

    @Test
    @DisplayName("UserType 업데이트 실패 - 유저 조회 실패")
    public void failModifyUserTypeNotFoundUser() {
        doReturn(Optional.empty()).when(userRepository).findByUserId("userId");

        final ServerException result = assertThrows(ServerException.class, () -> target.modifyUserType("userId", "USER"));

        assertThat(result.getErrorResult()).isEqualTo(ErrorCode.USER_NOT_FOUND);

        verify(userRepository, times(1)).findByUserId("userId");
    }

    @Test
    @DisplayName("UserType 업데이트 실패 - UserType 에러")
    public void failModifyUserTypeIllegalArgument() {
        doReturn(Optional.of(getUser())).when(userRepository).findByUserId("userId");

        final ServerException result = assertThrows(ServerException.class, () -> target.modifyUserType("userId", "test"));

        assertThat(result.getErrorResult()).isEqualTo(ErrorCode.ILLEGAL_ARGUMENT);

        verify(userRepository, times(1)).findByUserId("userId");
    }
}
