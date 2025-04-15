package com.jobster.entity;

import java.time.LocalDateTime;
import java.util.List;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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
@Table(name = "applicant_profile")
public class ApplicantProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "name is required")
    @Size(max = 100)
    private String name;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "email is required")
    @Email(message = "invalid email format")
    private String email;

    @NotBlank(message = "phonenumber is required")
    @Pattern(regexp = "^[0-9\\\\-\\\\+]{9,15}$", message = "enter a valid phonenumber")
    private String phoneNumber;

    @Size(max = 255)
    @NotBlank(message = "address is required")
    private String address;

    @Size(max = 500)
    @NotBlank(message = "bio is required")
    private String bio;

    @ElementCollection
    @CollectionTable(name = "applicant_skills",joinColumns = @JoinColumn(name="applicant_id"))
    @Column(name = "skill")
    @Size(min = 5, message = "atleast 5 skills are required")
    @NotEmpty(message = "atleast 5 skills are required")
    private List<String> skills;

    @NotNull(message = "resume is required")
    @Lob
    @Column(name = "resume_url",nullable = false)
    private byte[] resume;

    @Size(max = 255)
    @Pattern(regexp = ".*\\.(jpg|jpeg|png)$", message = "image should be in jpg,jpeg,png format")
    @NotBlank(message = "profilepicture is required")
    private String profilePic;

    @Min(value = 0, message = "experience should not be negative")
    private int experience;

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
