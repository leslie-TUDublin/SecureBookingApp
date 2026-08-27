package com.lesya.booking1.dto.auth;

import lombok.Getter;

// EN: DTO returned to Customer after successful registration or login (containes JWT token that Customer will use for authenticated requests).
@Getter
public class AuthResponse {

    // EN: JWT token generated for the authenticated user.
    // UA: JWT-токен, створений для авторизованого користувача.
    private final String token;

    // EN: Constructor used to create authentication response.
    // UA: Конструктор для створення відповіді після автентифікації.
    public AuthResponse(String token) {
        this.token = token;
    }
}