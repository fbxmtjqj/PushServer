package com.fbxmtjqj.pushserver.fcm.controller;

import com.fbxmtjqj.pushserver.fcm.model.dto.SendMessageRequest;
import com.fbxmtjqj.pushserver.fcm.model.dto.SendMessageResponse;
import com.fbxmtjqj.pushserver.fcm.service.MessageService;
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
public class MessageController {
    private final MessageService messageService;

    @PostMapping("/api/v1/send/message")
    public ResponseEntity<SendMessageResponse> sendMessage(
            @RequestBody @Valid final SendMessageRequest fcmRequest) throws IOException {

        final SendMessageResponse response = messageService.sendMessage(fcmRequest.getUserId(), fcmRequest.getContent());

        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }
}
