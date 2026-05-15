package com.flyaway.api.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterUserDTO {
    @NotBlank
    @Pattern(regexp = ".*[A-Z].*")
    private String firstName;

    @NotBlank
    @Pattern(regexp = ".*[A-Z].*")
    private String lastName;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d).{8,}$")
    private String password;
}