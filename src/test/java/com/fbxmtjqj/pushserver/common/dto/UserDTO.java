package com.fbxmtjqj.pushserver.common.dto;

import com.fbxmtjqj.pushserver.user.model.entity.Group;
import com.fbxmtjqj.pushserver.user.model.entity.User;
import com.fbxmtjqj.pushserver.user.model.enums.UserType;

public class UserDTO {
    public static User getUser() {
        return getUser("userId", "password", null, null);
    }

    public static User getUser(Group group) {
        return getUser("userId", "password", group, null);
    }

    public static User getUser(Group group, UserType userType) {
        return getUser("userId", "password", group, userType);
    }

    public static User getUser(String userId, String password, Group group, UserType userType) {
        return User.builder()
                .userId(userId)
                .password(password)
                .group(group)
                .userType(userType)
                .build();
    }
}
