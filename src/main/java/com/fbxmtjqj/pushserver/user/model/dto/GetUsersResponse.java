package com.fbxmtjqj.pushserver.user.model.dto;

import com.fbxmtjqj.pushserver.user.model.enums.UserType;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@Builder
@RequiredArgsConstructor
public class GetUsersResponse {
    private final String userId;
    private final UserType userType;
    private final String siteNm;
    private final List<String> key;
}
