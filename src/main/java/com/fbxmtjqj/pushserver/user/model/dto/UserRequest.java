package com.fbxmtjqj.pushserver.user.model.dto;

import com.fbxmtjqj.pushserver.user.model.validation.ValidationGroups;
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

    @NotNull(groups = {ValidationGroups.UserAddMarker.class, ValidationGroups.SignInMarker.class})
    private final String userId;

    @NotNull(groups = {ValidationGroups.UserAddMarker.class, ValidationGroups.SignInMarker.class})
    private final String password;

    @NotNull(groups = {ValidationGroups.UserAddMarker.class})
    private final String siteNm;

    private final String userType;
}
