package de.yagub.deliverysystem.msuser.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        String errorMessage = authException.getMessage();
        String errorCode = "INVALID_TOKEN";

        if (authException instanceof OAuth2AuthenticationException oauthEx) {
//            if (oauthEx.getError() != null) {
//                errorCode = oauthEx.getError().getErrorCode();
//            }

            if (oauthEx.getCause() instanceof JwtValidationException jwtEx) {
                if (jwtEx.getMessage().contains("JWT expired")) {
                    errorCode = "EXPIRED_TOKEN";
                    errorMessage = "The access token has expired";
                }
            }
        }
        response.getWriter().write(String.format(
                "{\"errorCode\":\"%s\", \"message\":\"%s\", \"uuid\":\"%s\", " +
                        "\"path\":\"%s\", \"timestamp\":\"%s\"}",
                errorCode,
                errorMessage,
                UUID.randomUUID(),
                request.getRequestURI(),
                Instant.now()
        ));
    }
}
