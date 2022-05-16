package com.fbxmtjqj.pushserver.user.repository;

import com.fbxmtjqj.pushserver.common.exception.ErrorCode;
import com.fbxmtjqj.pushserver.common.exception.ServerException;
import com.fbxmtjqj.pushserver.user.model.entity.User;
import com.fbxmtjqj.pushserver.user.model.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("유저등록")
    public void addUser() {
        final User user = getUser("userId", "password", "test");

        final User result = userRepository.save(user);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getUserId()).isEqualTo("userId");
        assertThat(result.getPassword()).isEqualTo("password");
        assertThat(result.getSiteNm()).isEqualTo("test");
    }

    @Test
    @DisplayName("유저조회")
    public void searchUser() {
        final User user = getUser("userId", "password", "test");

        userRepository.save(user);
        final User findResult = userRepository.findByUserId("userId")
                .orElseThrow(() -> new ServerException(ErrorCode.USER_NOT_FOUND));

        assertThat(findResult).isNotNull();
        assertThat(findResult.getUserId()).isEqualTo("userId");
        assertThat(findResult.getPassword()).isEqualTo("password");
        assertThat(findResult.getSiteNm()).isEqualTo("test");
        assertThat(findResult.getUserType()).isNull();
    }

    private User getUser(final String userId, final String password, final String siteNm) {
        return User.builder()
                .userId(userId)
                .password(password)
                .siteNm(siteNm)
                .build();
    }
}
