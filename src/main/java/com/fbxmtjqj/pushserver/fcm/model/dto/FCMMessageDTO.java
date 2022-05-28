package com.fbxmtjqj.pushserver.fcm.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class FCMMessageDTO {
    private boolean validateOnly;
    private Message message;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Message {
        private String token;
        private Notification data;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Notification {
        private String content;
    }
}
