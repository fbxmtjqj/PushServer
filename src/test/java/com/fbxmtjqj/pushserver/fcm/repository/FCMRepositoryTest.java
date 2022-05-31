package com.fbxmtjqj.pushserver.fcm.repository;

import com.fbxmtjqj.pushserver.common.TestRepositoryConfig;
import com.fbxmtjqj.pushserver.common.exception.ErrorCode;
import com.fbxmtjqj.pushserver.common.exception.ServerException;
import com.fbxmtjqj.pushserver.fcm.model.entity.FCM;
import com.fbxmtjqj.pushserver.fcm.model.repository.FCMRepository;
import com.fbxmtjqj.pushserver.user.model.entity.Group;
import com.fbxmtjqj.pushserver.user.model.entity.User;
import com.fbxmtjqj.pushserver.user.model.repository.GroupRepository;
import com.fbxmtjqj.pushserver.user.model.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
@Import(TestRepositoryConfig.class)
public class FCMRepositoryTest {

    @Autowired
    private FCMRepository fcmRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GroupRepository groupRepository;

    private final String token = "token";
    private final String userId = "userId";

    @Test
    @DisplayName("fcm 저장")
    public void saveFCM() {
        final User user = getUser();
        final FCM fcm = getFCM(user);

        final FCM result = fcmRepository.save(fcm);

        assertThat(result).isNotNull();
        assertThat(result.getToken()).isEqualTo(token);
        assertThat(result.getUser().getUserId()).isEqualTo(userId);
    }

    @Test
    @DisplayName("Key로 조회")
    public void findByKey() {
        final FCM fcm = FCM.builder().token(token).build();
        fcmRepository.save(fcm);

        final FCM result = fcmRepository.findByToken(token)
                .orElseThrow(() -> new ServerException(ErrorCode.TEST_ERROR));

        assertThat(result).isNotNull();
        assertThat(result.getToken()).isEqualTo(token);
    }

    @Test
    @DisplayName("UserId 조회")
    public void getFCMListByUserId() {
        final User user = getUser();
        final FCM fcm = getFCM(user);
        userRepository.save(user);
        fcmRepository.save(fcm);

        final List<FCM> result = fcmRepository.getFCMListByUserId(userId)
                .orElseThrow(() -> new ServerException(ErrorCode.TEST_ERROR));

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getToken()).isEqualTo(token);
        assertThat(result.get(0).getUser().getUserId()).isEqualTo(userId);
    }

    @Test
    @DisplayName("Key 삭제")
    public void deleteByKey() {
        final User user = getUser();
        final FCM fcm = getFCM(user);
        userRepository.save(user);
        fcmRepository.save(fcm);

        fcmRepository.deleteByToken(token);

        final List<FCM> result = fcmRepository.getFCMListByUserId(userId)
                .orElseThrow(() -> new ServerException(ErrorCode.TEST_ERROR));

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(0);
    }

    private User getUser() {
        Group group = Group.builder().name("group").build();
        groupRepository.save(group);
        return User.builder().userId(userId).password("password").group(group).build();
    }

    private FCM getFCM(final User user) {
        return FCM.builder().token(token).user(user).build();
    }
}
