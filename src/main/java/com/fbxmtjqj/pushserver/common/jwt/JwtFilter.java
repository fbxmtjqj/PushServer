package com.fbxmtjqj.pushserver.common.jwt;

import com.fbxmtjqj.pushserver.common.exception.ErrorCode;
import com.fbxmtjqj.pushserver.common.exception.ServerException;
import com.fbxmtjqj.pushserver.user.model.entity.User;
import com.fbxmtjqj.pushserver.user.model.enums.UserType;
import com.fbxmtjqj.pushserver.user.model.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Log4j2
@Transactional(readOnly = true)
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String method = request.getMethod();
        final String uri = request.getRequestURI();

        if (!requestMatch(method, uri)) {
            final String token = jwtService.parseToken(request);
            if (StringUtils.hasText(token) && jwtService.isValidAccessToken(token)) {
                try {
                    long id = jwtService.getId(token, true);
                    User userInfo = userRepository.findById(id).orElseThrow(() -> new ServerException(ErrorCode.USER_NOT_FOUND));
                    UsernamePasswordAuthenticationToken authentication = createAuthenticationToken(userInfo);
                    authentication.setDetails(userInfo.getUserId());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } catch (Exception ex) {
                    SecurityContextHolder.clearContext();
                    throw new ServerException(ErrorCode.INVALID_TOKEN);
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken createAuthenticationToken(User user) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (user.getUserType() == UserType.ADMIN) {
            authorities.add(new SimpleGrantedAuthority(UserType.ADMIN.toString()));
        }

        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(user.getUserId())
                .password(user.getPassword())
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private boolean requestMatch(final String method, final String uri) {
        final HashMap<String, String> requestMap = new HashMap<>() {{
            put(HttpMethod.POST.name(), "/api/v1/users,/api/v1/signIn");
            put(HttpMethod.GET.name(), "/refresh/accessToken");
        }};

        if (requestMap.containsKey(method)) {
            return requestMap.get(method).contains(uri);
        } else {
            return false;
        }
    }
}
