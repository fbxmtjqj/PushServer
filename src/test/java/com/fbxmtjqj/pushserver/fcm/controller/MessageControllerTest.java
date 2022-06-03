package com.fbxmtjqj.pushserver.fcm.controller;

import com.fbxmtjqj.pushserver.fcm.model.dto.SendMessageRequest;
import com.fbxmtjqj.pushserver.fcm.service.MessageService;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.stream.Stream;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class MessageControllerTest {
    @InjectMocks
    private MessageController target;
    @Mock
    private MessageService messageService;

    private MockMvc mockMvc;
    private Gson gson;

    @BeforeEach
    public void init() {
        gson = new Gson();
        mockMvc = MockMvcBuilders.standaloneSetup(target)
                .build();
    }

    @Test
    @DisplayName("SendMessage 성공")
    public void successSendMessage() throws Exception {
        final String url = "/api/v1/send/message";

        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(gson.toJson(SendMessageRequest.builder().userId("userId").content("content").build()))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        resultActions.andExpect(status().isOk());
    }

    @ParameterizedTest
    @MethodSource("failSendMessageParameter")
    @DisplayName("SendMessage 실패 - 잘못된 파라미터")
    public void failSendMessage(final String userId, final String content) throws Exception {
        final String url = "/api/v1/send/message";

        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(gson.toJson(SendMessageRequest.builder().userId(userId).content(content).build()))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        resultActions.andExpect(status().isBadRequest());
    }

    private static Stream<Arguments> failSendMessageParameter() {
        return Stream.of(
                Arguments.of(null, "test"),
                Arguments.of("test", null)
        );
    }
}
