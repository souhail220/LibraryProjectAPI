package com.library.libraryprojectapi.authentication.models;

import com.library.libraryprojectapi.data.models.RoleName;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterUserDTO {
    @NotBlank(message = "First name is required")
    @Size(min = 3, max = 20)
    private String firstname;

    @NotBlank(message = "Last name is required")
    @Size(min = 3, max = 20)
    private String lastname;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank
    @Size(min = 8, message = "Password must be at least 8 chars")
    private String password;

    @NotBlank
    @Size(min = 8, message = "Phone number must be at least 8 chars")
    private String phoneNumber;

    @NotNull(message = "Role must not be null")
    private RoleName role;
}
