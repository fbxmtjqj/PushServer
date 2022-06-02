package com.fbxmtjqj.pushserver.user.controller;

import com.fbxmtjqj.pushserver.user.model.dto.AddUserRequest;
import com.fbxmtjqj.pushserver.user.model.dto.GetUsersResponse;
import com.fbxmtjqj.pushserver.user.model.dto.SignInRequest;
import com.fbxmtjqj.pushserver.user.model.dto.UpdateUserRequest;
import com.fbxmtjqj.pushserver.user.services.UserReadService;
import com.fbxmtjqj.pushserver.user.services.UserService;
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

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    @InjectMocks
    private UserController target;
    @Mock
    private UserService userService;
    @Mock
    private UserReadService userReadService;

    private MockMvc mockMvc;
    private Gson gson;

    @BeforeEach
    public void init() {
        gson = new Gson();
        mockMvc = MockMvcBuilders.standaloneSetup(target)
                .build();
    }

    @Test
    @DisplayName("유저등록 성공")
    public void successAddUser() throws Exception {
        final String url = "/api/v1/users";

        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(gson.toJson(AddUserRequest.builder()
                                .userId("userId")
                                .password("password")
                                .siteNm("siteNm")
                                .build()))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        resultActions.andExpect(status().isCreated());
    }

    @ParameterizedTest
    @MethodSource("failAddUserParameter")
    @DisplayName("유저등록 실패 - 잘못된 파라미터")
    public void failAddUser(final String userId, final String password, final String siteNm) throws Exception {
        final String url = "/api/v1/users";

        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(gson.toJson(AddUserRequest.builder()
                                .userId(userId)
                                .password(password)
                                .siteNm(siteNm)
                                .build()))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("유저SignIn 성공")
    public void successSignIn() throws Exception {
        final String url = "/api/v1/signIn";

        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(gson.toJson(SignInRequest.builder()
                                .userId("userId")
                                .password("password")
                                .fcmKey("fcmKey")
                                .build()))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        resultActions.andExpect(status().isOk());
    }

    @ParameterizedTest
    @MethodSource("failSignInParameter")
    @DisplayName("유저SignIn 실패 - 잘못된 파라미터")
    public void failedSignIn(final String userId, final String password, final String fcmKey) throws Exception {
        final String url = "/api/v1/signIn";

        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(gson.toJson(SignInRequest.builder()
                                .userId(userId)
                                .password(password)
                                .fcmKey(fcmKey)
                                .build()))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("UserType 업데이트 성공")
    public void successUpdateUserType() throws Exception {
        final String url = "/api/v1/update/userType";

        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(gson.toJson(UpdateUserRequest.builder()
                                .userId("userId")
                                .userType("USER")
                                .build()))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        resultActions.andExpect(status().isOk());
    }

    @Test
    @DisplayName("유저목록조회 성공")
    public void successGetUsers() throws Exception {
        final String url = "/api/v1/users/any";
        doReturn(GetUsersResponse.builder().build()).when(userReadService).getUserByUserId(anyString());

        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
        );

        resultActions.andExpect(status().isOk());
    }

    private static Stream<Arguments> failAddUserParameter() {
        return Stream.of(
                Arguments.of(null, "test", "test"),
                Arguments.of("test", null, "test"),
                Arguments.of("test", "test", null)
        );
    }

    private static Stream<Arguments> failSignInParameter() {
        return Stream.of(
                Arguments.of(null, "test", "test"),
                Arguments.of("test", null, "test"),
                Arguments.of("test", "test", null)
        );
    }
}
