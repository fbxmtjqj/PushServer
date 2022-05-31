package com.fbxmtjqj.pushserver.fcm.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@Builder
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class AddTokenRequest {

    @NotNull
    private final String userId;

    @NotNull
    private final String token;
}
