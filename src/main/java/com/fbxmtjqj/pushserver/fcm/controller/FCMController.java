package com.fbxmtjqj.pushserver.fcm.controller;

import com.fbxmtjqj.pushserver.fcm.model.dto.FCMRequest;
import com.fbxmtjqj.pushserver.fcm.model.dto.SendMessageResponse;
import com.fbxmtjqj.pushserver.fcm.model.validation.ValidationFCM;
import com.fbxmtjqj.pushserver.fcm.service.FCMService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class FCMController {
    private final FCMService firebaseService;

    @PostMapping("/api/v1/send")
    public ResponseEntity<SendMessageResponse> sendMessage(
            @RequestBody @Validated(ValidationFCM.SendMessageMarker.class) final FCMRequest fcmRequest) throws IOException {

        final SendMessageResponse response = firebaseService.sendMessage(fcmRequest.getUserId(), fcmRequest.getContent());

        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }
}
