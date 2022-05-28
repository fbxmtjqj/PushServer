package com.fbxmtjqj.pushserver.fcm.model.dto;

import com.fbxmtjqj.pushserver.fcm.model.validation.ValidationFCM;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@Builder
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class FCMRequest {

    @NotNull(groups = {ValidationFCM.SendMessageMarker.class})
    private final String userId;

    @NotNull(groups = {ValidationFCM.SendMessageMarker.class})
    private final String content;
}
