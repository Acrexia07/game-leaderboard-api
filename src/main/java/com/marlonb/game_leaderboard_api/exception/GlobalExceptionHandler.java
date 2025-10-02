package com.marlonb.game_leaderboard_api.exception;

import com.marlonb.game_leaderboard_api.exception.custom.DuplicateResourceFoundException;
import com.marlonb.game_leaderboard_api.exception.custom.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.marlonb.game_leaderboard_api.exception.ErrorMessages.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    // HTTP STATUS 500 - INTERNAL SERVER ERROR
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handlesServerRelatedExceptions (Exception ex,
                                                                            HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body(new ErrorResponseDto
                                       (LocalDateTime.now(),
                                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                        INTERNAL_SERVER_ERROR_MESSAGE.getErrorMessage(),
                                        Map.of("server", List.of(ex.getMessage())),
                                        request.getRequestURI()));
    }

    // HTTP STATUS 409 - CONFLICT
    @ExceptionHandler(DuplicateResourceFoundException.class)
    public ResponseEntity<ErrorResponseDto> handlesDuplicateResourceException
            (DuplicateResourceFoundException ex,
             HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.CONFLICT)
                             .body(new ErrorResponseDto
                                       (LocalDateTime.now(),
                                        HttpStatus.CONFLICT.value(),
                                        DUPLICATE_RESOURCE_FOUND_MESSAGE.getErrorMessage(),
                                        Map.of("resource", List.of(ex.getMessage())),
                                        request.getRequestURI()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDto> handlesDataIntegrityExceptions (DataIntegrityViolationException ex,
                                                                            HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.CONFLICT)
                             .body(new ErrorResponseDto
                                       (LocalDateTime.now(),
                                        HttpStatus.CONFLICT.value(),
                                        DATA_INTEGRITY_ISSUE_MESSAGE.getErrorMessage(),
                                        Map.of("player",
                                                List.of(PLAYER_CREATION_ERROR_MESSAGE.getErrorMessage())),
                                        request.getRequestURI()));
    }

    // HTTP STATUS 404 - NOT FOUND
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handlesResourceNotFoundExceptions (ResourceNotFoundException ex,
                                                                               HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body(new ErrorResponseDto
                                       (LocalDateTime.now(),
                                        HttpStatus.NOT_FOUND.value(),
                                        RESOURCE_NOT_FOUND_MESSAGE.getErrorMessage(),
                                        Map.of("resource", List.of(ex.getMessage())),
                                        request.getRequestURI()));
    }

    // HTTP STATUS 403 - FORBIDDEN
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponseDto> handlesAccessDeniedExceptions (AccessDeniedException ex,
                                                                           HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponseDto
                        (LocalDateTime.now(),
                                HttpStatus.FORBIDDEN.value(),
                                ACCESS_DENIED_MESSAGE.getErrorMessage(),
                                Map.of("credentials", List.of(ex.getMessage())),
                                request.getRequestURI()));
    }

    // HTTP STATUS 401 - UNAUTHORIZED
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponseDto> handlesBadCredentialsExceptions (BadCredentialsException ex,
                                                                             HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                             .body(new ErrorResponseDto
                                       (LocalDateTime.now(),
                                        HttpStatus.UNAUTHORIZED.value(),
                                        BAD_CREDENTIALS_MESSAGE.getErrorMessage(),
                                        Map.of("user", List.of(ex.getMessage())),
                                        request.getRequestURI()));
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ErrorResponseDto> handlesHttpClientRelatedExceptions (HttpClientErrorException ex,
                                                                                HttpServletRequest request) {

        String httpRelatedErrorMessage = HttpClientErrorMessage.fromStatus((HttpStatus) ex.getStatusCode());

        return ResponseEntity.status(ex.getStatusCode())
                             .body(new ErrorResponseDto
                                       (LocalDateTime.now(),
                                        ex.getStatusCode().value(),
                                        httpRelatedErrorMessage,
                                        Map.of("error", List.of(ex.getMessage())),
                                        request.getRequestURI()));
    }

    // HTTP STATUS 400 - BAD REQUEST
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handlesAttributeValidationExceptions
            (MethodArgumentNotValidException ex,
             HttpServletRequest request) {

        Map<String, List<String>> fieldErrors = new HashMap<>();

        ex.getBindingResult()
          .getFieldErrors()
          .forEach(error -> {
                                      String fieldName = error.getField();
                                      String fieldMessage = error.getDefaultMessage();
                                      fieldErrors.computeIfAbsent(fieldName, fieldElement -> new ArrayList<>())
                                                 .add(fieldMessage);
                                      }
          );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(new ErrorResponseDto
                                      (LocalDateTime.now(),
                                       HttpStatus.BAD_REQUEST.value(),
                                       VALIDATION_ERROR_MESSAGE.getErrorMessage(),
                                       fieldErrors,
                                       request.getRequestURI()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDto> handlesUnreadableHttpMessageExceptions
            (HttpMessageNotReadableException ex,
             HttpServletRequest request) {

        Map<String, List<String>> customError;

        if (ex.getMessage().contains("JSON")) {
            customError = Map.of("json", List.of(JSON_ERROR_MESSAGE.getErrorMessage()));
        } else {
            customError = Map.of("request", List.of(FORMAT_REQUEST_ERROR_MESSAGE.getErrorMessage()));
        }

        return ResponseEntity.badRequest()
                             .body(new ErrorResponseDto
                                       (LocalDateTime.now(),
                                        HttpStatus.BAD_REQUEST.value(),
                                        HTTP_MESSAGE_NOT_READABLE_ERROR_MESSAGE.getErrorMessage(),
                                        customError,
                                        request.getRequestURI()));
    }
}
