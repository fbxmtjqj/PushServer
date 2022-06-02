package com.fbxmtjqj.pushserver.user.model.repository;

import com.fbxmtjqj.pushserver.user.model.entity.User;

import java.util.Optional;

public interface UserRepositoryCustom {

    Optional<User> getUserByUserId(final String userId);
}
