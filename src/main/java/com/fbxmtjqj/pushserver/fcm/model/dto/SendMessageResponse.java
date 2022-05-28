package com.fbxmtjqj.pushserver.fcm.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class SendMessageResponse {
    private final int successCount;
    private final int failCount;
}
