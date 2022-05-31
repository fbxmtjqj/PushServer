package com.fbxmtjqj.pushserver.fcm.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fbxmtjqj.pushserver.common.exception.ErrorCode;
import com.fbxmtjqj.pushserver.common.exception.ServerException;
import com.fbxmtjqj.pushserver.fcm.model.dto.SendMessageResponse;
import com.fbxmtjqj.pushserver.fcm.model.entity.FCM;
import com.fbxmtjqj.pushserver.fcm.model.repository.FCMRepository;
import com.fbxmtjqj.pushserver.fcm.model.repository.MessageRepository;
import com.fbxmtjqj.pushserver.user.model.entity.User;
import com.fbxmtjqj.pushserver.user.model.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    private MessageRepository messageRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private FirebaseService fireBaseService;

    @Test
    @DisplayName("메세지전송 성공 - HttpStatus200")
    public void successSendMessageHttpStatus200() throws JsonProcessingException {
        List<FCM> fcmList = new ArrayList<>();
        fcmList.add(FCM.builder().token("token").build());
        doReturn(Optional.of(fcmList)).when(fcmRepository).getFCMListByUserId("userId");
        doReturn(200).when(fireBaseService).sendMessage("token", "content");

        final SendMessageResponse result = target.sendMessage("userId", "content");

        assertThat(result).isNotNull();
        assertThat(result.getSuccessCount()).isEqualTo(1);
        assertThat(result.getFailCount()).isEqualTo(0);
    }

    @Test
    @DisplayName("메세지전송 성공 - HttpStatus401")
    public void successSendMessageHttpStatus401() throws JsonProcessingException {
        List<FCM> fcmList = new ArrayList<>();
        fcmList.add(FCM.builder().token("token").build());
        doReturn(Optional.of(fcmList)).when(fcmRepository).getFCMListByUserId("userId");
        doReturn(400).when(fireBaseService).sendMessage("token", "content");

        final SendMessageResponse result = target.sendMessage("userId", "content");

        assertThat(result).isNotNull();
        assertThat(result.getSuccessCount()).isEqualTo(0);
        assertThat(result.getFailCount()).isEqualTo(1);

        verify(fcmRepository, times(1)).deleteByToken("token");
    }

    @Test
    @DisplayName("메세지전송 성공 - HttpStatus404")
    public void successSendMessageHttpStatus404() throws JsonProcessingException {
        List<FCM> fcmList = new ArrayList<>();
        fcmList.add(FCM.builder().token("token").build());
        doReturn(Optional.of(fcmList)).when(fcmRepository).getFCMListByUserId("userId");
        doReturn(404).when(fireBaseService).sendMessage("token", "content");

        final SendMessageResponse result = target.sendMessage("userId", "content");

        assertThat(result).isNotNull();
        assertThat(result.getSuccessCount()).isEqualTo(0);
        assertThat(result.getFailCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("메세지전송 성공 - HttpStatusMix")
    public void successSendMessageHttpStatusMix() throws JsonProcessingException {
        List<FCM> fcmList = new ArrayList<>();
        fcmList.add(FCM.builder().token("token1").build());
        fcmList.add(FCM.builder().token("token2").build());
        doReturn(Optional.of(fcmList)).when(fcmRepository).getFCMListByUserId("userId");
        doReturn(200).when(fireBaseService).sendMessage("token1", "content");
        doReturn(404).when(fireBaseService).sendMessage("token2", "content");

        final SendMessageResponse result = target.sendMessage("userId", "content");

        assertThat(result).isNotNull();
        assertThat(result.getSuccessCount()).isEqualTo(1);
        assertThat(result.getFailCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("메세지전송 실패 - tokenList 조회 실패")
    public void failSendMessageTokenNotFound() {
        doReturn(Optional.empty()).when(fcmRepository).getFCMListByUserId("userId");

        final ServerException result = assertThrows(ServerException.class, () -> target.sendMessage("userId", "content"));

        assertThat(result.getErrorResult()).isEqualTo(ErrorCode.NOT_FOUND);
    }

    @Test
    @DisplayName("토큰추가 성공")
    public void successAddToken() {
        doReturn(Optional.of(User.builder().userId("userId").build())).when(userRepository).findByUserId("userId");

        target.addToken("userId","fcmToken");

        verify(fcmRepository, times(1)).save(any(FCM.class));
    }

    @Test
    @DisplayName("토큰추가 실패 - 유저 조회 실패")
    public void failAddToken() {
        doReturn(Optional.empty()).when(userRepository).findByUserId("userId");

        final ServerException result = assertThrows(ServerException.class, () -> target.addToken("userId","fcmToken"));

        assertThat(result.getErrorResult()).isEqualTo(ErrorCode.USER_NOT_FOUND);
    }

}
