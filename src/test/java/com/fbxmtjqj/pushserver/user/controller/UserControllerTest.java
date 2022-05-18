package com.fbxmtjqj.pushserver.user.controller;

import com.fbxmtjqj.pushserver.user.model.dto.GetUsersResponse;
import com.fbxmtjqj.pushserver.user.model.dto.UserRequest;
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

import java.util.Arrays;
import java.util.stream.Stream;

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
    @DisplayName("유저등록성공")
    public void successAddUser() throws Exception {
        final String url = "/api/v1/users";

        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(gson.toJson(getUser("test", "test", "test")))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        resultActions.andExpect(status().isCreated());
    }

    @ParameterizedTest
    @MethodSource("failedAddUserParameter")
    @DisplayName("유저등록실패")
    public void failedAddUser(final String userId, final String password, final String siteNm) throws Exception {
        final String url = "/api/v1/users";

        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(gson.toJson(getUser(userId, password, siteNm)))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("유저SignIn성공")
    public void successSignIn() throws Exception {
        final String url = "/api/v1/signIn";

        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(gson.toJson(getUser("test", "test")))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        resultActions.andExpect(status().isOk());
    }

    @ParameterizedTest
    @MethodSource("failedSignInParameter")
    @DisplayName("유저SignIn실패")
    public void failedSignIn(final String userId, final String password) throws Exception {
        final String url = "/api/v1/signIn";

        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(gson.toJson(getUser(userId, password)))
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
                        .content(gson.toJson(getUser("test", null, null, "USER")))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        resultActions.andExpect(status().isOk());
    }

    @Test
    @DisplayName("유저목록조회성공")
    public void successGetUsers() throws Exception {
        // given
        final String url = "/api/v1/users";
        doReturn(Arrays.asList(
                GetUsersResponse.builder().build(),
                GetUsersResponse.builder().build(),
                GetUsersResponse.builder().build()
        )).when(userReadService).getUsers();

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
        );

        // then
        resultActions.andExpect(status().isOk());
    }

    private UserRequest getUser(final String userId, final String password) {
        return getUser(userId, password, null, null);
    }

    private UserRequest getUser(final String userId, final String password, final String siteNm) {
        return getUser(userId, password, siteNm, null);
    }

    private UserRequest getUser(final String userId, final String password, final String siteNm, final String userType) {
        return UserRequest.builder()
                .userId(userId)
                .password(password)
                .siteNm(siteNm)
                .userType(userType)
                .build();
    }

    private static Stream<Arguments> failedAddUserParameter() {
        return Stream.of(
                Arguments.of(null, "test", "test"),
                Arguments.of("test", null, "test"),
                Arguments.of("test", "test", null)
        );
    }

    private static Stream<Arguments> failedSignInParameter() {
        return Stream.of(
                Arguments.of(null, "test"),
                Arguments.of("test", null)
        );
    }
}
