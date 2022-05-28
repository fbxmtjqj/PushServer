package com.fbxmtjqj.pushserver.fcm.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fbxmtjqj.pushserver.common.exception.ErrorCode;
import com.fbxmtjqj.pushserver.common.exception.ServerException;
import com.fbxmtjqj.pushserver.fcm.model.dto.FCMMessageDTO;
import com.fbxmtjqj.pushserver.fcm.model.dto.SendMessageResponse;
import com.fbxmtjqj.pushserver.fcm.model.entity.FCM;
import com.fbxmtjqj.pushserver.fcm.model.repository.FCMRepository;
import com.fbxmtjqj.pushserver.user.model.entity.User;
import com.fbxmtjqj.pushserver.user.model.repository.UserRepository;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.gson.JsonParseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

@Log4j2
@RequiredArgsConstructor
@Service
@Transactional
public class FCMService {
    private final FCMRepository fcmRepository;
    private final UserRepository userRepository;

    @Value("${fcm.url}")
    private String apiUrl;
    private ObjectMapper objectMapper;
    private String accessToken;

    public SendMessageResponse sendMessage(final String userId, final String content) throws IOException {
        final User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new ServerException(ErrorCode.USER_NOT_FOUND));
        final List<FCM> tokenList = fcmRepository.findByUser(user)
                .orElseThrow(() -> new ServerException(ErrorCode.FCM_NOT_FOUND));

        OkHttpClient client = new OkHttpClient();
        int successMessageCount = 0;
        int failMessageCount = 0;
        for (FCM token : tokenList) {
            String message = makeMessage(token.getKey(), content);
            RequestBody requestBody = RequestBody.create(message, MediaType.get("application/json; charset=utf-8"));
            Request request = new Request.Builder()
                    .url(apiUrl)
                    .post(requestBody)
                    .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    successMessageCount++;
                } else {
                    failMessageCount++;

                    if (response.code() == 401) {
                        fcmRepository.deleteByKey(token.getKey());
                    }
                }
            } catch (Exception ex) {
                failMessageCount++;
                log.error(ex);
            }
        }

        return SendMessageResponse.builder()
                .successCount(successMessageCount)
                .failCount(failMessageCount)
                .build();
    }

    private String makeMessage(final String targetToken, final String content) throws JsonParseException, JsonProcessingException {
        FCMMessageDTO fcmMessage = FCMMessageDTO.builder()
                .message(FCMMessageDTO.Message.builder()
                        .token(targetToken)
                        .data(FCMMessageDTO.Notification.builder()
                                .content(content)
                                .build())
                        .build()).validateOnly(false).build();

        return objectMapper.writeValueAsString(fcmMessage);
    }

    @PostConstruct
    private void initialize() throws IOException {
        objectMapper = new ObjectMapper();
        getFCMToken();
    }

    private void getFCMToken() throws IOException {
        final String firebaseConfigPath = "firebase/firebase_service_key.json";

        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

        googleCredentials.refreshIfExpired();
        accessToken = googleCredentials.getAccessToken().getTokenValue();

        log.info("=== token ===");
        log.info(accessToken);
    }
}
