package com.sparta.vicky.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtProvider {

    public static final String BEARER_PREFIX = "Bearer ";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String REFRESH_TOKEN_COOKIE_NAME = "RefreshToken";

    private final RefreshTokenService refreshTokenService;

    @Value("${jwt.secret.key}")
    private String secretKey;

    private Key key;

    @PostConstruct
    public void init() {
        key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKey));
    }

    /**
     * Access 토큰 생성
     */
    public String createAccessToken(String username) {
        Date date = new Date();

        // Access 토큰 만료기간 (30분)
        long ACCESS_TOKEN_TIME = 10 * 1000L;
//        long ACCESS_TOKEN_TIME = 30 * 60 * 1000L;

        return BEARER_PREFIX + Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(date.getTime() + ACCESS_TOKEN_TIME))
                .setIssuedAt(date) // 발급일
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Refresh 토큰 생성
     */
    public String createRefreshToken(String username) {
        Date date = new Date();

        // Refresh 토큰 만료기간 (2주)
        long REFRESH_TOKEN_TIME = 14 * 24 * 60 * 60 * 1000L;

        return BEARER_PREFIX + Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(date.getTime() + REFRESH_TOKEN_TIME))
                .setIssuedAt(date) // 발급일
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Cookie에 Refresh 토큰 저장
     */
    public void addJwtToCookie(String token, HttpServletResponse response) {
        String encodedToken = URLEncoder.encode(token, StandardCharsets.UTF_8)
                .replaceAll("\\+", "%20");

        Cookie cookie = new Cookie(REFRESH_TOKEN_COOKIE_NAME, encodedToken);
        cookie.setHttpOnly(true);
//        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(14 * 24 * 60 * 60); // 2주

        response.addCookie(cookie);
    }

    /**
     * Header에서 Access 토큰 가져오기
     */
    public String getAccessTokenFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        } else {
            return null;
        }
    }

    /**
     * HttpServletRequest에 들어있는 Cookie에서 Refresh 토큰 가져오기
     */
    public String getRefreshTokenFromRequest(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(REFRESH_TOKEN_COOKIE_NAME)) {
                String bearerToken = URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8);
                return bearerToken.substring(7);
            }
        }
        return null;
    }

    /**
     * Access 토큰 검증
     */
    public boolean validateAccessToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.error("유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.error("만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.error("잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    /**
     * Refresh 토큰 검증
     */
    public boolean validateRefreshToken(String token) {
        log.info("Refresh 토큰 검증");
        String username = getUsernameFromToken(token);
        return refreshTokenService.findByUsername(username).isPresent();
    }

    /**
     * 토큰에서 사용자 정보 가져오기
     */
    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * Access 토큰 헤더 설정
     */
    public void setHeaderAccessToken(HttpServletResponse response, String accessToken) {
        response.setHeader(AUTHORIZATION_HEADER, accessToken);
    }

}
