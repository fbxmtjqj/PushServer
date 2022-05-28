package com.fbxmtjqj.pushserver.fcm.model.repository;

import com.fbxmtjqj.pushserver.fcm.model.entity.FCM;
import com.fbxmtjqj.pushserver.user.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FCMRepository extends JpaRepository<FCM, Long> {

    Optional<FCM> findByKey(final String name);

    Optional<List<FCM>> findByUser(final User user);

    void deleteByKey(final String key);
}
