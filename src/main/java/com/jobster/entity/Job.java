package com.jobster.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "jobs")
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "title is required")
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    @NotBlank(message = "description is required")
    private String description;

    @Column(nullable = false)
    @NotBlank(message = "location is required")
    private String location;

    @Column(nullable = false)
    @Positive(message = "salary must be positive")
    @NotNull(message = "salary is required")
    private Double salary;

    @Column(nullable = false)
    @Min(value = 0, message = "experience should not be non-negative")
    @NotNull(message = "experience is required")
    private int experienceRequired;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull(message = "workmode is required")
    private WorkMode workMode;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull(message = "jobstatus is required")
    private JobStatus jobStatus;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime postedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employer_id", nullable = false)
    private EmployerProfile employerProfile;
}
