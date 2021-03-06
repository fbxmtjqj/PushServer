package com.fbxmtjqj.pushserver.user.services;

import com.fbxmtjqj.pushserver.common.exception.ErrorCode;
import com.fbxmtjqj.pushserver.common.exception.ServerException;
import com.fbxmtjqj.pushserver.common.jwt.JwtService;
import com.fbxmtjqj.pushserver.fcm.model.entity.FCM;
import com.fbxmtjqj.pushserver.fcm.model.repository.FCMRepository;
import com.fbxmtjqj.pushserver.user.model.dto.SignInResponse;
import com.fbxmtjqj.pushserver.user.model.entity.Group;
import com.fbxmtjqj.pushserver.user.model.entity.User;
import com.fbxmtjqj.pushserver.user.model.enums.UserType;
import com.fbxmtjqj.pushserver.user.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final FCMRepository fcmRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public void createUser(final String userId, final String password, final String groupNm) {
        boolean isUserId = userRepository.findByUserId(userId).isPresent();
        if (isUserId) {
            throw new ServerException(ErrorCode.USER_ALREADY_REGISTER);
        }

        Group group = Group.builder().name(groupNm).build();

        final User user = User.builder()
                .userId(userId)
                .password(passwordEncoder.encode(password))
                .group(group)
                .build();

        userRepository.save(user);
    }

    public SignInResponse signIn(final String userId, final String password, final String fcmToken) {
        final User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new ServerException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new ServerException(ErrorCode.USER_SIGN_IN_FAILED);
        }

        final String accessToken = jwtService.createAccessToken(user);
        final String refreshToken = jwtService.createRefreshToken(user);

        final FCM fcm = fcmRepository.findByToken(fcmToken).orElseGet(() -> FCM.builder().token(fcmToken).user(user).build());
        fcmRepository.save(fcm);

        return SignInResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public void modifyUserType(final String userId, final String type) {
        final User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new ServerException(ErrorCode.USER_NOT_FOUND));

        UserType userType;
        try {
            userType = UserType.valueOf(type);
        } catch (IllegalArgumentException ex) {
            throw new ServerException(ErrorCode.ILLEGAL_ARGUMENT);
        } catch (Exception ex) {
            throw new ServerException(ErrorCode.UNKNOWN_EXCEPTION);
        }

        user.setUserType(userType);

        userRepository.save(user);
    }
}
