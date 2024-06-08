package com.sparta.vicky.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.vicky.jwt.JwtProvider;
import com.sparta.vicky.user.dto.LoginRequest;
import com.sparta.vicky.user.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtProvider jwtProvider;

    @Autowired
    private UserService userService;

    public JwtAuthenticationFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
        setFilterProcessesUrl("/api/user/login");
    }

    /**
     * 로그인 시도
     */
    @Override
    public org.springframework.security.core.Authentication attemptAuthentication(
            HttpServletRequest req,
            HttpServletResponse res
    ) throws AuthenticationException {
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
    protected void successfulAuthentication(
            HttpServletRequest req,
            HttpServletResponse res,
            FilterChain chain, Authentication authResult
    ) throws IOException {

        UserDetailsImpl userDetails = (UserDetailsImpl) authResult.getPrincipal();
        String username = userDetails.getUsername();

        String accessToken = jwtProvider.createAccessToken(username);
        String refreshToken = jwtProvider.createRefreshToken(username);

        res.addHeader(JwtProvider.AUTHORIZATION_ACCESS_HEADER, accessToken);
        res.addHeader(JwtProvider.AUTHORIZATION_REFRESH_HEADER, refreshToken);

        // 사용자 개인 필드에 refreshToken 저장
        userService.saveRefreshToken(refreshToken, userDetails.getUser().getId());

        log.info("로그인 성공 : {}", username);

        // 응답 메시지 작성
        res.setStatus(SC_OK);
        res.setCharacterEncoding("UTF-8");
        res.setContentType("application/json");

        // JSON 응답 생성
        String jsonResponse = new ObjectMapper()
                .writeValueAsString(new ApiResponse(SC_OK, "로그인 성공", accessToken));

        res.getWriter().write(jsonResponse);
    }

    /**
     * 로그인 실패
     */
    protected void unsuccessfulAuthentication(
            HttpServletRequest req,
            HttpServletResponse res,
            AuthenticationException failed
    ) throws IOException {
        log.error("로그인 실패 : {}", failed.getMessage());

        // 응답 메시지 작성
        res.setStatus(SC_UNAUTHORIZED);
        res.setCharacterEncoding("UTF-8");
        res.setContentType("application/json");

        // JSON 응답 생성
        String jsonResponse = new ObjectMapper()
                .writeValueAsString(new ApiResponse(SC_UNAUTHORIZED, "로그인 실패: " + failed.getMessage(), null));

        res.getWriter().write(jsonResponse);
    }

    /**
     * 로그인 응답 데이터
     */
    @Data
    private static class ApiResponse {

        private int statusCode;
        private String msg;
        private String accessToken;

        public ApiResponse(int statusCode, String msg, String accessToken) {
            this.statusCode = statusCode;
            this.msg = msg;
            this.accessToken = accessToken;
        }

    }

}
