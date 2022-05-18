package com.fbxmtjqj.pushserver.user.controller;

import com.fbxmtjqj.pushserver.user.model.dto.*;
import com.fbxmtjqj.pushserver.user.model.validation.ValidationGroups;
import com.fbxmtjqj.pushserver.user.services.UserReadService;
import com.fbxmtjqj.pushserver.user.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserReadService userReadService;

    @GetMapping ("/api/v1/users")
    public ResponseEntity<List<GetUsersResponse>> getUsers() {

        final List<GetUsersResponse> getUsersResponseList = userReadService.getUsers();

        return ResponseEntity.status(HttpStatus.OK)
                .body(getUsersResponseList);
    }

    @PostMapping("/api/v1/users")
    public ResponseEntity<AddUserResponse> addUser(
            @RequestBody @Validated(ValidationGroups.UserAddMarker.class) final UserRequest userRequest) {

        final AddUserResponse userAddResponse = userService.addUser(userRequest.getUserId(), userRequest.getPassword(), userRequest.getSiteNm());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userAddResponse);
    }

    @PostMapping("/api/v1/signIn")
    public ResponseEntity<SignInResponse> signIn(
            @RequestBody @Validated(ValidationGroups.SignInMarker.class) final UserRequest userRequest) {

        final SignInResponse signInResponse = userService.signin(userRequest.getUserId(), userRequest.getPassword());

        return ResponseEntity.status(HttpStatus.OK)
                .body(signInResponse);
    }

    @PostMapping("/api/v1/update/userType")
    public ResponseEntity<UpdateUserTypeResponse> updateUserType(
            @RequestBody final UserRequest userRequest) {

        final UpdateUserTypeResponse result = userService.updateUserType(userRequest.getUserId(), userRequest.getUserType());

        return ResponseEntity.status(HttpStatus.OK)
                .body(result);
    }
}
