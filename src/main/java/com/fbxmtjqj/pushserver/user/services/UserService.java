package com.fbxmtjqj.pushserver.user.services;

import com.fbxmtjqj.pushserver.common.exception.ErrorCode;
import com.fbxmtjqj.pushserver.common.exception.ServerException;
import com.fbxmtjqj.pushserver.user.model.dto.SignInResponse;
import com.fbxmtjqj.pushserver.user.model.dto.UserAddResponse;
import com.fbxmtjqj.pushserver.user.model.entity.User;
import com.fbxmtjqj.pushserver.user.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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

    public SignInResponse singin(final String userId, final String password) {
        final Optional<User> userOpt = userRepository.findByUserId(userId);
        if (userOpt.isEmpty()) {
            throw new ServerException(ErrorCode.USER_NOT_FOUND);
        }

        final User user = userOpt.get();

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
}