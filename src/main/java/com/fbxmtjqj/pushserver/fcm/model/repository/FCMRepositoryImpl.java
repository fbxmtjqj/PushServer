package com.fbxmtjqj.pushserver.fcm.model.repository;

import com.fbxmtjqj.pushserver.fcm.model.entity.FCM;
import com.fbxmtjqj.pushserver.fcm.model.entity.QFCM;
import com.fbxmtjqj.pushserver.user.model.entity.QUser;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class FCMRepositoryImpl implements FCMRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    QFCM qFCM = QFCM.fCM;
    QUser qUser = QUser.user;

    @Override
    public Optional<List<FCM>> findFCMListByUserId(final String userId) {
        QUser user = new QUser("userGroup");
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(qFCM)
                        .where(qFCM.user.in(
                                JPAExpressions
                                        .select(qUser)
                                        .from(qUser, user)
                                        .where(qUser.group.eq(user.group)
                                                .and(user.userId.eq(userId)))
                        ))
                        .fetch());
    }
}
