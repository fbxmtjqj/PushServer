package com.fbxmtjqj.pushserver.common.jwt;

import com.fbxmtjqj.pushserver.user.model.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@Service
public class JwtService implements InitializingBean {
    @Value("${jwt.max-age.access}")
    private long accessExpiredSec;

    @Value("${jwt.max-age.refresh}")
    private long refreshExpiredSec;

    @Value("${jwt.key.access}")
    private String accessKey;

    @Value("${jwt.key.refresh}")
    private String refreshKey;

    private Key atKey;
    private Key rtKey;

    @Override
    public void afterPropertiesSet() {
        byte[] atSecretBytes = Decoders.BASE64.decode(accessKey);
        atKey = Keys.hmacShaKeyFor(atSecretBytes);

        byte[] rtSecretBytes = Decoders.BASE64.decode(refreshKey);
        rtKey = Keys.hmacShaKeyFor(rtSecretBytes);
    }

    public String createAccessToken(final User user) {
        return createToken(accessExpiredSec, atKey, user);
    }

    public String createRefreshToken(final User user) {
        return createToken(refreshExpiredSec, rtKey, user);
    }

    private String createToken(final long expiredSec, final Key key, final User user) {
        Instant now = Instant.now();
        Instant expired = now.plusSeconds(expiredSec);

        Map<String, String> payloads = new HashMap<>();
        payloads.put("value1", user.getId().toString());
        payloads.put("value2", user.getUserId());

        return Jwts.builder()
                .setClaims(payloads)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expired))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String parseToken(HttpServletRequest request) {
        final String token = request.getHeader("Authorization");
        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            return token.substring(7);
        }

        return null;
    }

    public long getId(final String token, final boolean isAccessToken) {
        final Claims claims = Jwts.parserBuilder()
                .setSigningKey((isAccessToken ? atKey : rtKey))
                .build()
                .parseClaimsJws(token)
                .getBody();

        final String value = claims.get("value1", String.class);

        return Long.parseLong(value);
    }

    public boolean isValidRefreshToken(String token) {
        return isValidToken(token, rtKey);
    }

    public boolean isValidAccessToken(String token) {
        return isValidToken(token, atKey);
    }

    private boolean isValidToken(final String token, final Key key) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SecurityException except) {
            log.warn("Invalid JWT signature");
        } catch (MalformedJwtException except) {
            log.warn("Invalid JWT token");
        } catch (ExpiredJwtException except) {
            log.warn("Expired JWT token");
        } catch (UnsupportedJwtException except) {
            log.warn("Unsupported JWT token");
        } catch (IllegalArgumentException except) {
            log.warn("JWT token compact of handler are invalid");
        }
        return false;
    }
}
