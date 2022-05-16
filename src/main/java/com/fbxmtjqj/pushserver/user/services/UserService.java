package com.fbxmtjqj.pushserver.user.services;

import com.fbxmtjqj.pushserver.common.exception.ErrorCode;
import com.fbxmtjqj.pushserver.common.exception.ServerException;
import com.fbxmtjqj.pushserver.user.model.dto.SignInResponse;
import com.fbxmtjqj.pushserver.user.model.dto.UserAddResponse;
import com.fbxmtjqj.pushserver.user.model.entity.User;
import com.fbxmtjqj.pushserver.user.model.enums.UserType;
import com.fbxmtjqj.pushserver.user.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public UserAddResponse addUser(final String userId, final String password, final String siteNm) {
        final User user = User.builder()
                .userId(userId)
                .password(passwordEncoder.encode(password))
                .siteNm(siteNm)
                .build();

        userRepository.save(user);

        return UserAddResponse.builder()
                .httpStatus(HttpStatus.OK)
                .build();
    }

    public SignInResponse signin(final String userId, final String password) {
        final User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new ServerException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new ServerException(ErrorCode.USER_SIGN_IN_FAILED);
        }

        final String accessToken = tokenService.createToken("access", user.getUserId());
        final String refreshToken = tokenService.createToken("refresh", user.getUserId());

        return SignInResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public void updateUserType(final String userId, final UserType userType) {
        final User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new ServerException(ErrorCode.USER_NOT_FOUND));

        user.setUserType(userType);

        userRepository.save(user);
    }
}
