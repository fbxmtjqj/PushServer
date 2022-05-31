package com.fbxmtjqj.pushserver.fcm.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fbxmtjqj.pushserver.common.exception.ErrorCode;
import com.fbxmtjqj.pushserver.common.exception.ServerException;
import com.fbxmtjqj.pushserver.fcm.model.dto.SendMessageResponse;
import com.fbxmtjqj.pushserver.fcm.model.entity.FCM;
import com.fbxmtjqj.pushserver.fcm.model.entity.Message;
import com.fbxmtjqj.pushserver.fcm.model.repository.FCMRepository;
import com.fbxmtjqj.pushserver.fcm.model.repository.MessageRepository;
import com.fbxmtjqj.pushserver.user.model.entity.User;
import com.fbxmtjqj.pushserver.user.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class FCMService {
    private final FCMRepository fcmRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;

    private final FirebaseService fireBaseService;

    public SendMessageResponse sendMessage(final String userId, final String content) throws JsonProcessingException {
        final List<FCM> tokenList = fcmRepository.getFCMListByUserId(userId)
                .orElseThrow(() -> new ServerException(ErrorCode.NOT_FOUND));

        int successMessageCount = 0;
        int failMessageCount = 0;
        for (FCM token : tokenList) {
            final String fcmToken = token.getToken();
            final int responseHttpStatusCode = fireBaseService.sendMessage(fcmToken, content);

            if (HttpStatus.valueOf(responseHttpStatusCode).is2xxSuccessful()) {
                successMessageCount++;
            } else {
                failMessageCount++;
                if (responseHttpStatusCode == 400 || responseHttpStatusCode == 404) {
                    fcmRepository.deleteByToken(fcmToken);
                }
            }
        }

        messageRepository.save(Message.builder()
                                    .user(tokenList.get(0).getUser())
                                    .content(content)
                                    .successCount(successMessageCount)
                                    .failCount(failMessageCount)
                                    .build());

        return SendMessageResponse.builder()
                .successCount(successMessageCount)
                .failCount(failMessageCount)
                .build();
    }

    public void addToken(final String userId, final String fcmToken) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(()-> new ServerException(ErrorCode.USER_NOT_FOUND));

        fcmRepository.save(FCM.builder()
                .user(user)
                .token(fcmToken)
                .build());
    }
}
