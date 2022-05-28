package com.fbxmtjqj.pushserver.fcm.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fbxmtjqj.pushserver.fcm.model.dto.FCMMessageDTO;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.gson.JsonParseException;
import lombok.extern.log4j.Log4j2;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

@Log4j2
@Service
public class FirebaseService {

    @Value("${fcm.url}")
    private String apiUrl;
    private String accessToken;
    private ObjectMapper objectMapper;
    private final OkHttpClient client = new OkHttpClient();

    public int sendMessage(final String fcmToken, final String content) throws JsonProcessingException {
        String message = makeMessage(fcmToken, content);
        RequestBody requestBody = RequestBody.create(message, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(apiUrl)
                .post(requestBody)
                .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.code();
        } catch (Exception ex) {
            log.error(ex);
        }

        return 0;
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
