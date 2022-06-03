package com.fbxmtjqj.pushserver.fcm.service;

import com.fbxmtjqj.pushserver.common.exception.ErrorCode;
import com.fbxmtjqj.pushserver.common.exception.ServerException;
import com.fbxmtjqj.pushserver.fcm.model.entity.FCM;
import com.fbxmtjqj.pushserver.fcm.model.repository.FCMRepository;
import com.fbxmtjqj.pushserver.user.model.entity.User;
import com.fbxmtjqj.pushserver.user.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class FCMService {
    private final FCMRepository fcmRepository;
    private final UserRepository userRepository;

    public void inputFCMToken(final String userId, final String fcmToken) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(()-> new ServerException(ErrorCode.USER_NOT_FOUND));

        fcmRepository.save(FCM.builder()
                .user(user)
                .token(fcmToken)
                .build());
    }
}
