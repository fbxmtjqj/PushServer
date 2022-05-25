package com.fbxmtjqj.pushserver.user.services;

import com.fbxmtjqj.pushserver.user.model.dto.GetUsersResponse;
import com.fbxmtjqj.pushserver.user.model.entity.User;
import com.fbxmtjqj.pushserver.user.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
                        .siteNm(v.getGroup())
                        .build())
                .collect(Collectors.toList());
    }
}
