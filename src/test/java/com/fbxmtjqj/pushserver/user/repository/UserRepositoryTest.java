package com.fbxmtjqj.pushserver.user.repository;

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
    @DisplayName("맴버십등록")
    public void addUser(){
        final User user = User.builder()
                .userId("userid")
                .password("password")
                .siteNm("test")
                .build();

        final User result = userRepository.save(user);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getUserId()).isEqualTo("userid");
        assertThat(result.getPassword()).isEqualTo("password");
        assertThat(result.getSiteNm()).isEqualTo("test");
    }
}
