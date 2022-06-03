package com.fbxmtjqj.pushserver.fcm.controller;

import com.fbxmtjqj.pushserver.fcm.model.dto.AddTokenRequest;
import com.fbxmtjqj.pushserver.fcm.service.FCMService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class FCMController {
    private final FCMService firebaseService;

    @PostMapping("/api/v1/input/fcmToken")
    public ResponseEntity<Void> inputFCMToken(
            @RequestBody @Valid AddTokenRequest tokenRequest) {

        firebaseService.inputFCMToken(tokenRequest.getUserId(), tokenRequest.getToken());

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
