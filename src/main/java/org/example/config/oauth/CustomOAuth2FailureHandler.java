package org.example.config.oauth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import java.io.IOException;

public class CustomOAuth2FailureHandler extends SimpleUrlAuthenticationFailureHandler {
    private static final String FRONTEND_ERROR_URL = "https://coffeechat.shop";

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        // 특정 URL에 대해 리다이렉트 처리
        if (request.getRequestURI().contains("/login")) {
            getRedirectStrategy().sendRedirect(request, response, FRONTEND_ERROR_URL);
        } else {
            super.onAuthenticationFailure(request, response, exception);
        }
    }
}
