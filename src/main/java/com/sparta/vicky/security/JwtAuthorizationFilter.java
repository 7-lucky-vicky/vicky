package com.sparta.vicky.security;

import com.sparta.vicky.jwt.JwtProvider;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j(topic = "JWT 검증 및 인가")
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthorizationFilter(JwtProvider jwtProvider, UserDetailsServiceImpl userDetailsService) {
        this.jwtProvider = jwtProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {

        String accessTokenValue = jwtProvider.getAccessTokenFromHeader(req);
        String refreshTokenValue = jwtProvider.getRefreshTokenFromHeader(req);

        if (StringUtils.hasText(accessTokenValue)) {
            // access 토큰이 유효하지 않을 때
            if ("fail".equals(jwtProvider.validateToken(accessTokenValue))) {
                log.error("Token Unsupported");
                return;
            }
            // access 토큰이 유효할 때
            else if("pass".equals(jwtProvider.validateToken(accessTokenValue))) {
                Claims info = jwtProvider.getUserInfoFromToken(accessTokenValue);

                try {
                    setAuthentication(info.getSubject());
                } catch (Exception e) {
                    log.error(e.getMessage());
                    return;
                }
            }
            // access 토큰이 expired 되었을 때
            else if("expired".equals(jwtProvider.validateToken(accessTokenValue))) {
                log.error("Access Token Expired");
                if(StringUtils.hasText(refreshTokenValue)) {
                    Claims info = jwtProvider.getUserInfoFromToken(refreshTokenValue);

                    //refresh 토큰이 유효할 때
                    if(jwtProvider.validateRefreshToken(refreshTokenValue, info.getSubject())) {

                        String accessToken = jwtProvider.createAccessToken(info.getSubject());
                        String refreshToken = jwtProvider.createRefreshToken(info.getSubject());

                        res.addHeader(JwtProvider.AUTHORIZATION_ACCESS_HEADER, accessToken);
                        res.addHeader(JwtProvider.AUTHORIZATION_REFRESH_HEADER, refreshToken);

                        try {
                            setAuthentication(info.getSubject());
                        } catch (Exception e) {
                            log.error(e.getMessage());
                            return;
                        }
                    }
                    else{
                        log.error("Refresh Token Expired");
                        return;
                    }
                }
            }
        }

        filterChain.doFilter(req, res);
    }

    /**
     * 인증 처리
     */
    public void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(username);

        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    /**
     * 인증 객체 생성
     */
    private Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
