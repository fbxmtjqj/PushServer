package com.fbxmtjqj.pushserver.user.services;

import com.fbxmtjqj.pushserver.fcm.model.entity.FCM;
import com.fbxmtjqj.pushserver.user.model.dto.GetUsersResponse;
import com.fbxmtjqj.pushserver.user.model.entity.User;
import com.fbxmtjqj.pushserver.user.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserReadService {
    private final UserRepository userRepository;

    public List<GetUsersResponse> getUsers() {
        final List<User> userList = userRepository.findAll();

        return userList.stream()
                .map(v -> GetUsersResponse.builder()
                        .userId(v.getUserId())
                        .userType(v.getUserType())
                        .siteNm(v.getGroup().getName())
                        .key(v.getFcm().stream().map(FCM::getKey).collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());
    }
}
