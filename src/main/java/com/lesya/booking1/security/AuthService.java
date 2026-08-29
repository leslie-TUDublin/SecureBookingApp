package com.lesya.booking1.security;

import com.lesya.booking1.dto.auth.AuthResponse;
import com.lesya.booking1.dto.auth.LoginRequest;
import com.lesya.booking1.dto.auth.RegisterRequest;
import com.lesya.booking1.entity.User;
import com.lesya.booking1.exception.InvalidCredentialsException;
import com.lesya.booking1.exception.UserAlreadyExistsException;
import com.lesya.booking1.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            AuthenticationManager authenticationManager
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    // REGISTER USER
    @Transactional
    public AuthResponse register(RegisterRequest request) {

        // EN: Check if email is already taken.
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User with email " + request.getEmail() + " already exists!");
        }

        // EN: Create and populate new User entity.
        User user = new User();
        user.setEmail(request.getEmail());
        // EN: Encode password with BCrypt.
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        // EN: Save the user into PostgreSQL.
        User savedUser = userRepository.save(user);

        // EN: Generate JWT token for the newly registered user.
        String jwtToken = jwtService.generateToken(savedUser.getEmail());

        // EN: Return response DTO matching our AuthResponse structure.
        return new AuthResponse(
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getRole().name(),
                jwtToken
        );
    }

    // LOGIN USER
    // EN: Authenticates user credentials , returns a JWT token.
    public AuthResponse login(LoginRequest request) {
        try {
            // EN: AuthenticationManager verifies if the password matches the email in DB.
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (AuthenticationException e) {
            // EN: Map Spring's exception to Custom exception (HTTP 401).
            throw new InvalidCredentialsException("Invalid email or password!");
        }

        // EN: If authentication successful, fetch user details to generate token.
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("User not found after authentication!"));

        // EN: Generate new JWT token.
        String jwtToken = jwtService.generateToken(user.getEmail());

        // EN: Return successful authentication payload.
        return new AuthResponse(
                user.getId(),
                user.getEmail(),
                user.getRole().name(),
                jwtToken
        );
    }
}
