package com.fbxmtjqj.pushserver.fcm.model.repository;

import com.fbxmtjqj.pushserver.fcm.model.entity.FCM;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FCMRepository extends JpaRepository<FCM, Long>, FCMRepositoryCustom {

    Optional<FCM> findByKey(final String name);

    void deleteByKey(final String key);
}
