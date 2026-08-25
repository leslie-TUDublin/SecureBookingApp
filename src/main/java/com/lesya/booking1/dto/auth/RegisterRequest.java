package com.lesya.booking1.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.lesya.booking1.entity.Role;

//  DTO  for creating a new user account.
@Getter
@Setter
@NoArgsConstructor
public class RegisterRequest {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    // EN: Role assigned to the new user.
    // UA: Роль, яка буде призначена новому користувачу.
    @NotNull
    private Role role;
}
