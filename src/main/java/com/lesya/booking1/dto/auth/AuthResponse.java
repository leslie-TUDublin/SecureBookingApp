package com.lesya.booking1.dto.auth;

import lombok.Getter;

// EN: DTO returned to the client after successful authentication.
// UA: DTO, який повертається клієнту після успішної автентифікації.
@Getter
public class AuthResponse {

    private final Long userId;
    private final String email;
    private final String role;

    public AuthResponse(
            Long userId,
            String email,
            String role
    ) {
        this.userId = userId;
        this.email = email;
        this.role = role;
    }
}