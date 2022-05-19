package com.fbxmtjqj.pushserver.token.controller;

import com.fbxmtjqj.pushserver.token.service.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class TokenControllerTest {
    @InjectMocks
    private TokenController target;
    @Mock
    private TokenService tokenService;

    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(target)
                .build();
    }

    @Test
    @DisplayName("accessToken 재발급 성공")
    public void successAddUser() throws Exception {
        final String url = "/refresh/accessToken";

        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
                        .header("Authorization", "refreshToken")
        );

        resultActions.andExpect(status().isOk());
    }

    @Test
    @DisplayName("accessToken 재발급 성공 - 헤더 없음")
    public void failAccessTokenHeaderNotFound() throws Exception {
        final String url = "/refresh/accessToken";

        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
        );

        resultActions.andExpect(status().isBadRequest());
    }
}
