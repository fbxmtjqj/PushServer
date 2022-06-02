package com.fbxmtjqj.pushserver.user.model.repository;

import com.fbxmtjqj.pushserver.fcm.model.entity.QFCM;
import com.fbxmtjqj.pushserver.user.model.entity.QGroup;
import com.fbxmtjqj.pushserver.user.model.entity.QUser;
import com.fbxmtjqj.pushserver.user.model.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public UserRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    QFCM qFCM = QFCM.fCM;
    QUser qUser = QUser.user;
    QGroup qGroup = QGroup.group;

    @Override
    public Optional<User> getUserByUserId(final String userId) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(qUser)
                        .join(qUser.group, qGroup).fetchJoin()
                        .where(qUser.userId.eq(userId))
                        .fetchOne());
    }
}
