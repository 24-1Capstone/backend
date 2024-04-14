package org.example.config.oauth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.config.jwt.TokenProvider;
import org.example.user.repository.token.RefreshTokenRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class OAuth2LogoutSuccessHandler implements LogoutSuccessHandler {

    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenProvider tokenProvider;
    private final static String HEADER_AUTHORIZATION = "Authorization";
    private final static String TOKEN_PREFIX = "Bearer ";

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String accessToken = getAccessToken(request.getHeader(HEADER_AUTHORIZATION));
        if (accessToken == null || !tokenProvider.validToken(accessToken)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().print("Invalid or missing token, logout failed.");
            response.getWriter().flush();
            return;
        }

        // Token is valid
        Long userId = tokenProvider.getUserId(accessToken);
        refreshTokenRepository.findByUserId(userId).ifPresent(refreshToken -> {
            refreshTokenRepository.delete(refreshToken);
        });

        // Complete the logout process
        new SecurityContextLogoutHandler().logout(request, response, authentication);
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().print("You have been logged out successfully.");
        response.getWriter().flush();
    }
    private String getAccessToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith(TOKEN_PREFIX)) {
            return authorizationHeader.substring(TOKEN_PREFIX.length());
        }
        return null;
    }
}