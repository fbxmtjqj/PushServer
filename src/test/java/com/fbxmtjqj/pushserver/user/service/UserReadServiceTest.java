package com.fbxmtjqj.pushserver.user.service;

import com.fbxmtjqj.pushserver.user.model.dto.GetUsersResponse;
import com.fbxmtjqj.pushserver.user.model.enums.UserType;
import com.fbxmtjqj.pushserver.user.model.repository.UserRepository;
import com.fbxmtjqj.pushserver.user.services.UserReadService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.fbxmtjqj.pushserver.common.dto.GroupDTO.getGroup;
import static com.fbxmtjqj.pushserver.common.dto.UserDTO.getUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserReadServiceTest {
    @InjectMocks
    private UserReadService target;
    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("유저조회 성공")
    public void successAddUser() {
        doReturn(Optional.of(getUser(getGroup(), UserType.USER))).when(userRepository).getUserByUserId("userId");

        final GetUsersResponse result = target.getUserByUserId("userId");

        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isEqualTo("userId");
        assertThat(result.getUserType()).isEqualTo(UserType.USER);
        assertThat(result.getGroup()).isEqualTo("group");

        verify(userRepository, times(1)).getUserByUserId(anyString());
    }
}
