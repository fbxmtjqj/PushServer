package com.fbxmtjqj.pushserver.user.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@Builder
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class UpdateUserRequest {

    @NotNull
    private final String userId;

    @NotNull
    private final String userType;

}
