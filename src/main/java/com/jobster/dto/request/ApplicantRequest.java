package com.jobster.dto.request;

import java.util.List;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicantRequest {

    @NotBlank(message = "name is required")
    @Size(max = 100)
    private String name;

    @NotBlank(message = "email is required")
    @Email(message = "invalid email format")
    private String email;

    @NotBlank(message = "phonenumber is required")
    @Pattern(regexp = "^[0-9\\\\-\\\\+]{9,15}$", message = "enter a valid phonenumber")
    private String phoneNumber;

    @NotBlank(message = "address is required")
    @Size(max = 255)
    private String address;

    @NotBlank(message = "bio is required")
    @Size(max = 500)
    private String bio;

    @NotNull(message = "skills are required")
    @Size(min = 5, message = "atleast 5 skills are required")
    private List<String> skills;

    @NotBlank(message = "resume is required")
    private String resume;

    @NotBlank(message = "profilepicture is required")
    @Size(max = 255)
    @Pattern(regexp = ".*\\.(jpg|jpeg|png)$", message = "image should be in jpg,jpeg,png format")
    private String profilePic;

    @Min(value = 0, message = "experience should not be negative")
    private int experience;
}
