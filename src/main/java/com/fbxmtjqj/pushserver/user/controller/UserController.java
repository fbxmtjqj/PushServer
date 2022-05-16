package com.fbxmtjqj.pushserver.user.controller;

import com.fbxmtjqj.pushserver.user.model.dto.SignInResponse;
import com.fbxmtjqj.pushserver.user.model.dto.UserAddResponse;
import com.fbxmtjqj.pushserver.user.model.dto.UserRequest;
import com.fbxmtjqj.pushserver.user.model.validation.ValidationGroups;
import com.fbxmtjqj.pushserver.user.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/api/v1/addUser")
    public ResponseEntity<UserAddResponse> addUser(
            @RequestBody @Validated(ValidationGroups.UserAddMarker.class) final UserRequest userRequest) {

        final UserAddResponse userAddResponse = userService.addUser(userRequest.getUserId(), userRequest.getPassword(), userRequest.getSiteNm());

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
}
