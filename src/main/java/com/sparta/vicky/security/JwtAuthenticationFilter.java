package com.sparta.vicky.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.vicky.jwt.JwtProvider;
import com.sparta.vicky.user.dto.LoginRequest;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j(topic="로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtProvider jwtProvider;

    public JwtAuthenticationFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
        setFilterProcessesUrl("/users/login");
    }

    /**
     * 로그인 시도
     */
    @Override
    public org.springframework.security.core.Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws org.springframework.security.core.AuthenticationException {
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
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain, Authentication authResult) {
        String username = ((UserDetailsImpl) authResult.getPrincipal()).getUsername();

        String accessToken = jwtProvider.createAccessToken(username);
        String refreshToken = jwtProvider.createRefreshToken(username);
        res.addHeader(JwtProvider.AUTHORIZATION_ACCESS_HEADER, accessToken);
        res.addHeader(JwtProvider.AUTHORIZATION_REFRESH_HEADER, refreshToken);
        log.info("로그인 성공 : {}", username);
    }

    /**
     * 로그인 실패
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest req, HttpServletResponse res, AuthenticationException failed) {
        log.error("로그인 실패 : {}", failed.getMessage());
        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

}
