package com.lesya.booking1.controller;

import com.lesya.booking1.dto.auth.AuthResponse;
import com.lesya.booking1.dto.auth.LoginRequest;
import com.lesya.booking1.dto.auth.RegisterRequest;
import com.lesya.booking1.security.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
// REST Controller for managing user authentication and registration.
public class AuthController {

    private final AuthService authService;


    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // REGISTER NEW USER
    // Endpoint for registering a new user. Available to everyone.
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // LOGIN USER
    // Endpoint for logging in an existing user. Returns JWT if credentials are valid.
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}
