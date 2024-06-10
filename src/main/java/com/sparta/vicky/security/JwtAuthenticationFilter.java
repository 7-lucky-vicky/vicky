package com.sparta.vicky.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.vicky.jwt.JwtProvider;
import com.sparta.vicky.jwt.RefreshTokenService;
import com.sparta.vicky.user.dto.LoginRequest;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

import static jakarta.servlet.http.HttpServletResponse.SC_OK;
import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtProvider jwtProvider;

    @Autowired
    private RefreshTokenService refreshTokenService;

    public JwtAuthenticationFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
        setFilterProcessesUrl("/api/user/login");
    }

    /**
     * 로그인 시도
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException {
        try {
            // json to object
            LoginRequest requestDto = new ObjectMapper()
                    .readValue(req.getInputStream(), LoginRequest.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getUsername(),
                            requestDto.getPassword(),
                            null
                    )
            );
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 로그인 성공 및 JWT 생성
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        String username = ((UserDetailsImpl) authResult.getPrincipal()).getUsername();

        String accessToken = jwtProvider.createAccessToken(username);
        String refreshToken = jwtProvider.createRefreshToken(username);

        // 헤더에 액세스 토큰 추가
        response.addHeader(JwtProvider.AUTHORIZATION_HEADER, accessToken);

        // 쿠키에 리프레시 토큰 추가
        jwtProvider.addJwtToCookie(refreshToken, response);

        // DB에 리프레시 토큰이 이미 있으면 수정, 없으면 저장
        refreshTokenService.save(username, refreshToken);

        log.info("로그인 성공 : {}", username);

        // 응답 메시지 작성
        response.setStatus(SC_OK);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        // JSON 응답 생성
        String jsonResponse = new ObjectMapper().writeValueAsString(
                new ApiResponse(SC_OK, "로그인 성공", accessToken, refreshToken)
        );

        response.getWriter().write(jsonResponse);
    }

    /**
     * 로그인 실패
     */
    protected void unsuccessfulAuthentication(HttpServletRequest req, HttpServletResponse res, AuthenticationException failed) throws IOException {
        log.error("로그인 실패 : {}", failed.getMessage());

        // 응답 메시지 작성
        res.setStatus(SC_UNAUTHORIZED);
        res.setCharacterEncoding("UTF-8");
        res.setContentType("application/json");

        // JSON 응답 생성
        String jsonResponse = new ObjectMapper().writeValueAsString(
                new ApiResponse(SC_UNAUTHORIZED, "로그인 실패: " + failed.getMessage(), null, null)
        );

        res.getWriter().write(jsonResponse);
    }

    /**
     * 로그인 응답 데이터
     */
    @Data
    @AllArgsConstructor
    private static class ApiResponse {

        private int statusCode;
        private String msg;
        private String accessToken;
        private String refreshToken;

    }

}
