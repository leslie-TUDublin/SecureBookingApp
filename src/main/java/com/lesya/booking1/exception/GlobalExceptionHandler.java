package com.lesya.booking1.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

// EN: The main HANDLER that intercepts/HANDLES errors in the APP and converts them into clean, structured JSON responses.
// UA: Головний обробник, який перехоплює помилки в додатку та конвертує їх у чисті, структуровані JSON-відповіді.
@RestControllerAdvice
public class GlobalExceptionHandler {

    // EN: Handles cases when a user tries to register with an email that is already taken.
    // Intercepts: UserAlreadyExistsException
    // Returns: HTTP Status 409 (Conflict) + JSON body with error details.
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponse> handleUserAlreadyExists(UserAlreadyExistsException ex) {
        ApiErrorResponse response = new ApiErrorResponse(ex.getMessage(), HttpStatus.CONFLICT.value());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    // EN: Handles authentication failures (wrong email or password during login).
    // Intercepts: InvalidCredentialsException
    // Returns: HTTP Status 401 (Unauthorized) + JSON body with error details.
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidCredentials(InvalidCredentialsException ex) {
        ApiErrorResponse response = new ApiErrorResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED.value());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    // EN: Handles cases when a requested database entity (like a booking, room, or user) is not found.
    // Intercepts: ResourceNotFoundException
    // Returns: HTTP Status 404 (Not Found) + JSON body with error details.
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        ApiErrorResponse response = new ApiErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    //  HANDLER FOR DTO VALIDATION
    // EN: Handles DTO validation errors (e.g., @NotBlank, @Email, @Future).
    // Intercepts: MethodArgumentNotValidException
    // Returns: HTTP Status 400 (Bad Request) + clean list of validation messages.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        // EN: Collect all validation error messages into a single string.
        // UA: Збираємо всі повідомлення про помилки валідації в один рядок.
        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));

        ApiErrorResponse response = new ApiErrorResponse(errorMessage, HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}