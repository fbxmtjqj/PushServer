package com.fbxmtjqj.pushserver.fcm.repository;

import com.fbxmtjqj.pushserver.common.exception.ErrorCode;
import com.fbxmtjqj.pushserver.common.exception.ServerException;
import com.fbxmtjqj.pushserver.fcm.model.entity.FCM;
import com.fbxmtjqj.pushserver.fcm.model.repository.FCMRepository;
import com.fbxmtjqj.pushserver.user.model.entity.User;
import com.fbxmtjqj.pushserver.user.model.repository.UserRepository;
import com.google.firebase.auth.UserRecord;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
public class FCMRepositoryTest {
    @Autowired
    private FCMRepository fcmRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Key로 조회")
    public void findByKey() {
        final String key = "test";
        final FCM fcm = FCM.builder().key(key).build();
        fcmRepository.save(fcm);

        final FCM result = fcmRepository.findByKey(key)
                .orElseThrow(() -> new ServerException(ErrorCode.TEST_ERROR));

        assertThat(result).isNotNull();
        assertThat(result.getKey()).isEqualTo(key);
    }

    @Test
    @DisplayName("User로 조회 ")
    public void findByUser() {
        final String key = "test";
        final User user = User.builder().userId("userId").password("password").build();
        final FCM fcm = FCM.builder().key(key).user(user).build();
        userRepository.save(user);
        fcmRepository.save(fcm);

        final List<FCM> result = fcmRepository.findByUser(user)
                .orElseThrow(() -> new ServerException(ErrorCode.TEST_ERROR));

        assertThat(result).isNotNull();
        assertThat(result.get(0).getKey()).isEqualTo(key);
        assertThat(result.get(0).getUser()).isEqualTo(user);
    }

    @Test
    @DisplayName("Key 삭제")
    public void deleteByKey() {
        final String key = "test";
        final User user = User.builder().userId("userId").password("password").build();
        final FCM fcm = FCM.builder().key(key).user(user).build();
        userRepository.save(user);
        fcmRepository.save(fcm);

        fcmRepository.deleteByKey(key);

        final List<FCM> result = fcmRepository.findByUser(user)
                .orElseThrow(() -> new ServerException(ErrorCode.TEST_ERROR));
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(0);
    }
}
