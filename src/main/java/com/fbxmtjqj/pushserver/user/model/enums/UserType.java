package com.fbxmtjqj.pushserver.user.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserType {

    ADMIN("admin"),
    DEVELOPER("developer"),
    USER("user"),
    ;

    private final String userType;
}
