package com.lesya.booking1.dto.auth;

import lombok.Getter;

@Getter
// EN: DTO returned to the client after successful authentication (with JWT support).
// UA: DTO, який повертається клієнту після успішної автентифікації (із підтримкою JWT).
public class AuthResponse {

    private final Long userId;
    private final String email;
    private final String role;
    // EN: JWT Token required for authorizing subsequent requests.
    // UA: JWT Токен, необхідний для авторизації наступних запитів клієнта.
    private final String token;

    public AuthResponse(
            Long userId,
            String email,
            String role,
            String token
    ) {
        this.userId = userId;
        this.email = email;
        this.role = role;
        this.token = token;
    }
}