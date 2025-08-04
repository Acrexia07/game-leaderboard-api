package com.marlonb.game_leaderboard_api.exception;

import com.marlonb.game_leaderboard_api.exception.custom.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // HTTP STATUS 500 - INTERNAL SERVER ERROR
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handlesServerRelatedExceptions (Exception ex) {

        final String INTERNAL_SERVER_ERROR_MESSAGE = "Internal server issue found!";
        final String GENERIC_ERROR_MESSAGE = "An unexpected error occurred";

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body(new ErrorResponseDto
                                   (LocalDateTime.now(),
                                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                    INTERNAL_SERVER_ERROR_MESSAGE,
                                    Map.of("server", List.of(GENERIC_ERROR_MESSAGE))));
    }


    // HTTP STATUS 404 - NOT FOUND
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handlesResourceNotFoundExceptions (ResourceNotFoundException ex) {

        final String RESOURCE_NOT_FOUND_MESSAGE = "Resource not found!";

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body(new ErrorResponseDto
                                   (LocalDateTime.now(),
                                    HttpStatus.NOT_FOUND.value(),
                                    RESOURCE_NOT_FOUND_MESSAGE,
                                    Map.of("resource", List.of(ex.getMessage()))));
    }

    // HTTP STATUS 400 - BAD REQUEST
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handlesAttributeValidationExceptions (MethodArgumentNotValidException ex) {

        Map<String, List<String>> fieldErrors = new HashMap<>();
        final String VALIDATION_ERROR_MESSAGE = "Validation error/s found!";

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
                                   VALIDATION_ERROR_MESSAGE,
                                   fieldErrors));
    }

}
