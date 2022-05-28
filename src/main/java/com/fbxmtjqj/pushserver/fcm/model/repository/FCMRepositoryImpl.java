package com.fbxmtjqj.pushserver.fcm.model.repository;

import com.fbxmtjqj.pushserver.fcm.model.entity.FCM;
import com.fbxmtjqj.pushserver.fcm.model.entity.QFCM;
import com.fbxmtjqj.pushserver.user.model.entity.QUser;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class FCMRepositoryImpl implements FCMRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    public FCMRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    QFCM qFCM = QFCM.fCM;
    QUser qUser = QUser.user;

    @Override
    public Optional<List<FCM>> getFCMListByUserId(final String userId ) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(qFCM)
                        .join(qFCM.user, qUser)
                        .on(qUser.userId.eq(userId))
                        .fetch());
    }
}
