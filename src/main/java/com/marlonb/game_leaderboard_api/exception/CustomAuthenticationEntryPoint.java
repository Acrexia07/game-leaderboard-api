package com.marlonb.game_leaderboard_api.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static com.marlonb.game_leaderboard_api.exception.AuthenticationErrorMessages.*;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        String errorMessage = getAuthenticationString(authException);

        ErrorResponseDto errorResponse = new ErrorResponseDto(
                LocalDateTime.now(),
                HttpStatus.UNAUTHORIZED.value(),
                AUTHENTICATION_MAIN_MESSAGE.getErrorMessage(),
                Map.of("authentication", List.of(errorMessage)),
                request.getRequestURI()
        );

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        response.getWriter().write(mapper.writeValueAsString(errorResponse));
    }

    // Authentication Error String Helper
    private static String getAuthenticationString(AuthenticationException authException) {

        String errorMessage;
        String authExceptionMessage = authException.getMessage();

        if (authExceptionMessage.contains("expired")) {
            errorMessage = EXPIRED_TOKEN.getErrorMessage();
        } else if (authExceptionMessage.contains("Invalid")) {
            errorMessage = INVALID_TOKEN.getErrorMessage();
        } else if (authExceptionMessage.contains("Full authentication")) {
            errorMessage = FULL_AUTHENTICATION.getErrorMessage();
        } else {
            errorMessage = authExceptionMessage;
        }
        return errorMessage;
    }
}
