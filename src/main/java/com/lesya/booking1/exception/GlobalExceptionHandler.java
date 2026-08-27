package com.lesya.booking1.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//EN: The main HANDLER that intercepts/HANDLES  errors in the APP and and converts them into clean, structured JSON responses.
@RestControllerAdvice
public class GlobalExceptionHandler {

    //EN: Handles cases when a user tries to register with an email that is already taken.
    //     Intercepts: UserAlreadyExistsException
    //     Returns: HTTP Status 409 (Conflict) + JSON body with error details.
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponse> handleUserAlreadyExists(UserAlreadyExistsException ex) {
        // EN: Create a standard error response object using HTTP 409 value
        ApiErrorResponse response = new ApiErrorResponse(ex.getMessage(), HttpStatus.CONFLICT.value());

        // EN: Return the response wrapped in a ResponseEntity with CONFLICT status
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }


    // EN: Handles authentication failures (wrong email or password during login).
    //      Intercepts: InvalidCredentialsException
    //      Returns: HTTP Status 401 (Unauthorized) + JSON body with error details.
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidCredentials(InvalidCredentialsException ex) {
        //EN: Create a standard error response object using HTTP 401 value
        ApiErrorResponse response = new ApiErrorResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED.value());

        //EN: Return the response wrapped in a ResponseEntity with UNAUTHORIZED status
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    // EN: Handles cases when a requested database entity (like a booking, room, or user) is not found.
    //     Intercepts: ResourceNotFoundException
    //     Returns: HTTP Status 404 (Not Found) + JSON body with error details.
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        ApiErrorResponse response = new ApiErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value());
        // EN: Return the response wrapped in a ResponseEntity with NOT_FOUND status
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
