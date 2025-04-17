package com.jobster.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.jobster.converter.JsonConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
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
@Entity
@Table(name = "employer_profile")
public class EmployerProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "employername is required")
    @Size(max = 200)
    private String employerName;

    @NotBlank(message = "companyname is required")
    @Size(max = 200)
    private String companyName;

    @NotBlank(message = "companylocation is required")
    @Size(max = 200)
    private String companyLocation;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "email is required")
    @Email(message = "invalid email format")
    private String email;

    @Size(max = 255)
    @Pattern(regexp = ".*\\.(jpg|jpeg|png)$", message = "image should be in jpg,jpeg,png format")
    @NotBlank(message = "company logo is required")
    private String companyLogo;

    @Size(max = 255)
    @Pattern(regexp = ".*\\.(jpg|jpeg|png)$", message = "image should be in jpg,jpeg,png format")
    @NotBlank(message = "employer profilepicture is required")
    private String employerProfilePic;

    @Column(columnDefinition = "TEXT")
    @Convert(converter = JsonConverter.class)
    private Map<String,List<String>> companyTechStack;

    @Column(nullable = false,updatable = false)
    private LocalDateTime createdAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "user is required")
    private User user;

    @PrePersist
    protected void onCreated(){
        this.createdAt = LocalDateTime.now();
    }
}
