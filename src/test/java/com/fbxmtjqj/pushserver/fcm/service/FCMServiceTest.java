package com.fbxmtjqj.pushserver.fcm.service;

import com.fbxmtjqj.pushserver.common.exception.ErrorCode;
import com.fbxmtjqj.pushserver.common.exception.ServerException;
import com.fbxmtjqj.pushserver.fcm.model.entity.FCM;
import com.fbxmtjqj.pushserver.fcm.model.repository.FCMRepository;
import com.fbxmtjqj.pushserver.user.model.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.fbxmtjqj.pushserver.common.dto.UserDTO.getUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FCMServiceTest {
    @InjectMocks
    private FCMService target;
    @Mock
    private FCMRepository fcmRepository;
    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("토큰추가 성공")
    public void successAddToken() {
        doReturn(Optional.of(getUser())).when(userRepository).findByUserId("userId");

        target.inputFCMToken("userId", "fcmToken");

        verify(fcmRepository, times(1)).save(any(FCM.class));
    }

    @Test
    @DisplayName("토큰추가 실패 - 유저 조회 실패")
    public void failAddToken() {
        doReturn(Optional.empty()).when(userRepository).findByUserId("userId");

        final ServerException result = assertThrows(ServerException.class, () -> target.inputFCMToken("userId", "fcmToken"));

        assertThat(result.getErrorResult()).isEqualTo(ErrorCode.USER_NOT_FOUND);
    }

}
