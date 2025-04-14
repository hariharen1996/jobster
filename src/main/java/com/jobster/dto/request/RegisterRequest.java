package com.jobster.dto.request;

import com.jobster.entity.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "username is required")
    private String username;

    @NotBlank(message = "email is required")
    @Email(message = "invalid email")
    private String email;

    @NotBlank(message = "password is required")
    @Size(min = 8,message = "password should be atleast 8 characters")
    private String password;

    @NotBlank(message = "confirmpassword is required")
    private String confirmPassword;

    @NotNull(message = "role s required")
    private Role role;
}
