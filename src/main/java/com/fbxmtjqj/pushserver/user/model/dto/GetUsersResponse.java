package com.fbxmtjqj.pushserver.user.model.dto;

import com.fbxmtjqj.pushserver.user.model.enums.UserType;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class GetUsersResponse {
    private final String userId;
    private final UserType userType;
    private final String siteNm;
}
