package com.fbxmtjqj.pushserver.fcm.controller;

import com.fbxmtjqj.pushserver.fcm.model.dto.AddTokenRequest;
import com.fbxmtjqj.pushserver.fcm.model.dto.SendMessageRequest;
import com.fbxmtjqj.pushserver.fcm.model.dto.SendMessageResponse;
import com.fbxmtjqj.pushserver.fcm.service.FCMService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class FCMController {
    private final FCMService firebaseService;

    @PostMapping("/api/v1/send")
    public ResponseEntity<SendMessageResponse> sendMessage(
            @RequestBody @Valid final SendMessageRequest fcmRequest) throws IOException {

        final SendMessageResponse response = firebaseService.sendMessage(fcmRequest.getUserId(), fcmRequest.getContent());

        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @PostMapping("/api/v1/token")
    public ResponseEntity<Void> addToken(
            @RequestBody @Valid AddTokenRequest tokenRequest) {

        firebaseService.addToken(tokenRequest.getUserId(), tokenRequest.getToken());

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
