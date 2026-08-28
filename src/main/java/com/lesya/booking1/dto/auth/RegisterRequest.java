package com.lesya.booking1.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.lesya.booking1.entity.Role;

@Getter
@Setter
@NoArgsConstructor
// EN: DTO for creating a new user account during registration.
// UA: DTO для створення нового облікового запису користувача під час реєстрації.
public class RegisterRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    @NotNull(message = "Role is required")
    // EN: Role assigned to the new user (e.g., USER, ADMIN).
    // UA: Роль, яка буде призначена новому користувачу (наприклад, USER, ADMIN).
    private Role role;
}