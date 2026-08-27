package com.lesya.booking1.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// EN: DTO used for login requests.
// UA: DTO який використовується для запиту входу в систему.
@Getter
@Setter
@NoArgsConstructor
public class LoginRequest {

    // EN: Email cannot be empty and must have a valid email format.
    // UA: Email не може бути порожнім і повинен мати правильний формат.
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    private String email;

    // EN: Password cannot be empty.
    // UA: Пароль не може бути порожнім.
    @NotBlank(message = "Password cannot be empty")
    private String password;
}
