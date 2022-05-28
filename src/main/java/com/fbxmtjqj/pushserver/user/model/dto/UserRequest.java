package com.fbxmtjqj.pushserver.user.model.dto;

import com.fbxmtjqj.pushserver.user.model.validation.ValidationUser;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@Builder
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class UserRequest {

    @NotNull(groups = {ValidationUser.UserAddMarker.class, ValidationUser.SignInMarker.class})
    private final String userId;

    @NotNull(groups = {ValidationUser.UserAddMarker.class, ValidationUser.SignInMarker.class})
    private final String password;

    @NotNull(groups = {ValidationUser.UserAddMarker.class})
    private final String siteNm;

    private final String userType;

    @NotNull(groups = {ValidationUser.SignInMarker.class})
    private final String fcmKey;
}
