package com.fbxmtjqj.pushserver.fcm.model.repository;

import com.fbxmtjqj.pushserver.fcm.model.entity.FCM;

import java.util.List;
import java.util.Optional;

public interface FCMRepositoryCustom {

    Optional<List<FCM>> getFCMListByUserId(final String userId);
}
