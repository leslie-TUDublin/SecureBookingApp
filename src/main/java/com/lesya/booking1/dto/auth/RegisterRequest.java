package com.lesya.booking1.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// EN: DTO  for creating a new user account.
@Getter
@Setter
@NoArgsConstructor
public class RegisterRequest {

    // EN: Email cannot be empty and must have a valid email format.
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    private String email;

    // EN: Password must contain at least 8 characters,including 4 security rules
    @NotBlank(message = "Password cannot be empty")
    @Pattern(
            //4 security rules: uppercase letter, lowercase letter, digit, special character.
            //4 правила безпеки - велика літера, мала, цифра, спецсимвол.
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must be at least 8 characters and include uppercase, lowercase, number and special character"
    )
    private String password;
}