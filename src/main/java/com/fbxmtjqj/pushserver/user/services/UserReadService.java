package com.fbxmtjqj.pushserver.user.services;

import com.fbxmtjqj.pushserver.common.exception.ErrorCode;
import com.fbxmtjqj.pushserver.common.exception.ServerException;
import com.fbxmtjqj.pushserver.user.model.dto.GetUsersResponse;
import com.fbxmtjqj.pushserver.user.model.entity.User;
import com.fbxmtjqj.pushserver.user.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserReadService {
    private final UserRepository userRepository;

    public GetUsersResponse getUserByUserId(final String userId) {
        final User userList = userRepository.findByUserId(userId)
                .orElseThrow(() -> new ServerException(ErrorCode.USER_NOT_FOUND));

        return GetUsersResponse.builder()
                .userId(userList.getUserId())
                .userType(userList.getUserType())
                .siteNm(userList.getGroup().getName())
                .build();
    }
}
