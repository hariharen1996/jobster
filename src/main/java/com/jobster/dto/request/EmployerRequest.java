package com.jobster.dto.request;


import java.util.List;
import java.util.Map;
import jakarta.validation.constraints.Email;
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
public class EmployerRequest {

    
    @NotBlank(message = "employername is required")
    @Size(max = 200)
    private String employerName;
    
    @NotBlank(message = "companyname is required")
    @Size(max = 200)
    private String companyName;

    @NotBlank(message = "companylocation is required")
    @Size(max = 200)
    private String companyLocation;


    @NotBlank(message = "email is required")
    @Email(message = "invalid email format")
    private String email;

    @NotBlank(message = "companylogo is required")
    @Size(max = 255)
    @Pattern(regexp = ".*\\.(jpg|jpeg|png)$", message = "image should be in jpg,jpeg,png format")
    private String companyLogo;

    @NotBlank(message = "profilepicture is required")
    @Size(max = 255)
    @Pattern(regexp = ".*\\.(jpg|jpeg|png)$", message = "image should be in jpg,jpeg,png format")
    private String employerProfilePic;

   
    @NotNull(message = "company techstack is required")
    @Size(min = 1,message = "minimum 1 techstack is required")
    private Map<String,List<String>> companyTechStack;
}
