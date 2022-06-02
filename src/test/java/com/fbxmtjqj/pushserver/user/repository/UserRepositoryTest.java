package com.fbxmtjqj.pushserver.user.repository;

import com.fbxmtjqj.pushserver.common.TestRepositoryConfig;
import com.fbxmtjqj.pushserver.common.exception.ErrorCode;
import com.fbxmtjqj.pushserver.common.exception.ServerException;
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

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
@Import(TestRepositoryConfig.class)
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GroupRepository groupRepository;

    @Test
    @DisplayName("유저등록")
    public void userSave() {
        final LocalDateTime now = LocalDateTime.now().withNano(0);
        final User user = getUser();

        final User result = userRepository.save(user);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getUserId()).isEqualTo("userId");
        assertThat(result.getPassword()).isEqualTo("password");
        assertThat(result.getCreatedDate().withNano(0)).isEqualTo(now);
        assertThat(result.getModifiedDate().withNano(0)).isEqualTo(now);
    }

    @Test
    @DisplayName("유저조회")
    public void findByUserId() {
        final User user = getUser();

        userRepository.save(user);
        final User findResult = userRepository.findByUserId("userId")
                .orElseThrow(() -> new ServerException(ErrorCode.TEST_ERROR));

        assertThat(findResult).isNotNull();
        assertThat(findResult.getUserId()).isEqualTo("userId");
        assertThat(findResult.getPassword()).isEqualTo("password");
        assertThat(findResult.getUserType()).isNull();
    }


    @Test
    @DisplayName("유저조회 - queryDsl")
    public void getUserByUserId() {
        final User user = getUser();

        userRepository.save(user);
        final User findResult = userRepository.getUserByUserId("userId")
                .orElseThrow(() -> new ServerException(ErrorCode.TEST_ERROR));

        assertThat(findResult).isNotNull();
        assertThat(findResult.getUserId()).isEqualTo("userId");
        assertThat(findResult.getPassword()).isEqualTo("password");
        assertThat(findResult.getGroup().getName()).isEqualTo("group");
        assertThat(findResult.getUserType()).isNull();
    }

    private User getUser() {
        Group group = Group.builder().name("group").build();
        groupRepository.save(group);
        return User.builder()
                .userId("userId")
                .password("password")
                .group(group)
                .build();
    }
}
