package com.sparta.vicky.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.vicky.jwt.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthorizationFilter(JwtProvider jwtProvider, UserDetailsServiceImpl userDetailsService) {
        this.jwtProvider = jwtProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String accessToken = jwtProvider.getAccessTokenFromHeader(request);
        String refreshToken = jwtProvider.getRefreshTokenFromRequest(request);

        if (StringUtils.hasText(accessToken)) {
            if (jwtProvider.validateAccessToken(accessToken)) {
                log.info("액세스 토큰 검증 성공");
                setAuthentication(jwtProvider.getUsernameFromToken(accessToken));
            }
            else if (StringUtils.hasText(refreshToken)) {
                log.info("액세스 토큰 만료 & 리프레시 토큰 존재");
                if (jwtProvider.validateRefreshToken(refreshToken)) {
                    log.info("리프레시 토큰 검증 성공");
                    // 새로운 액세스 토큰 발급
                    String username = jwtProvider.getUsernameFromToken(refreshToken);
                    String newAccessToken = jwtProvider.createAccessToken(username);

                    // 헤더에 새로운 액세스 토큰 설정
                    jwtProvider.setHeaderAccessToken(response, newAccessToken);

                    setAuthentication(username);
                }
                // 리프레시 토큰 만료 or 검증 실패
                else {
                    log.info("리프레시 토큰 검증 실패");
                    jwtExceptionHandler(response, "유효하지 않은 Refresh 토큰입니다.", HttpStatus.UNAUTHORIZED);
                    return;
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    /**
     * 인증 처리
     */
    public void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        context.setAuthentication(createAuthentication(username));

        SecurityContextHolder.setContext(context);
    }

    /**
     * 인증 객체 생성
     */
    private Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    /**
     * JWT 예외 처리
     */
    public void jwtExceptionHandler(HttpServletResponse response, String msg, HttpStatus status) {
        response.setStatus(status.value());
        response.setContentType("application/json");
        try {
            String json = new ObjectMapper()
                    .writeValueAsString(new HttpResponse(msg, status.value()));

            response.getWriter().write(json);

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Getter
    @NoArgsConstructor
    public static class HttpResponse {

        private int statusCode;
        private String msg;

        public HttpResponse(String msg, int statusCode) {
            this.statusCode = statusCode;
            this.msg = msg;
        }
    }

}
