package com.fbxmtjqj.pushserver.user.controller;

import com.fbxmtjqj.pushserver.user.model.dto.*;
import com.fbxmtjqj.pushserver.user.services.UserReadService;
import com.fbxmtjqj.pushserver.user.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserReadService userReadService;

    @GetMapping("/api/v1/users/{userId}")
    public ResponseEntity<GetUsersResponse> findUserByUserId(
            @Valid @PathVariable final String userId) {

        final GetUsersResponse getUsersResponseList = userReadService.findUserByUserId(userId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(getUsersResponseList);
    }

    @PostMapping("/api/v1/users")
    public ResponseEntity<Void> createUser(
            @RequestBody @Valid final AddUserRequest addUserRequest) {

        userService.createUser(addUserRequest.getUserId(), addUserRequest.getPassword(), addUserRequest.getSiteNm());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/api/v1/signIn")
    public ResponseEntity<SignInResponse> signIn(
            @RequestBody @Valid final SignInRequest signInRequest) {

        final SignInResponse signInResponse = userService.signIn(signInRequest.getUserId(), signInRequest.getPassword(), signInRequest.getFcmKey());

        return ResponseEntity.status(HttpStatus.OK)
                .body(signInResponse);
    }

    @PostMapping("/api/v1/modify/userType")
    public ResponseEntity<Void> modifyUserType(
            @RequestBody @Valid final UpdateUserRequest updateUserRequest) {

        userService.modifyUserType(updateUserRequest.getUserId(), updateUserRequest.getUserType());

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
