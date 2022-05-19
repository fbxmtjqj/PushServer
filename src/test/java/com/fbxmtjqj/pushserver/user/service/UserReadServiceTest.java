package com.fbxmtjqj.pushserver.user.service;

import com.fbxmtjqj.pushserver.user.model.dto.GetUsersResponse;
import com.fbxmtjqj.pushserver.user.model.entity.User;
import com.fbxmtjqj.pushserver.user.model.repository.UserRepository;
import com.fbxmtjqj.pushserver.user.services.UserReadService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

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
        doReturn(Arrays.asList(
                User.builder().build(),
                User.builder().build(),
                User.builder().build()
        )).when(userRepository).findAll();

        final List<GetUsersResponse> result = target.getUsers();

        assertThat(result.size()).isEqualTo(3);

        verify(userRepository, times(1)).findAll();
    }
}
