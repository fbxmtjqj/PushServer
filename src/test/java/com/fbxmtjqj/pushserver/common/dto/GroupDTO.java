package com.fbxmtjqj.pushserver.common.dto;

import com.fbxmtjqj.pushserver.user.model.entity.Group;

public class GroupDTO {
    public static Group getGroup() {
        return getGroup("group");
    }

    public static Group getGroup(String name) {
        return Group.builder().name(name).build();
    }
}
