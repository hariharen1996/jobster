package com.jobster.dto.request;


import com.jobster.entity.JobStatus;
import com.jobster.entity.WorkMode;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobRequest {
    @NotBlank(message = "title is required")
    @Size(min = 5, max = 100, message = "title should be between 5 and 100 characters long")
    private String title;

    @NotBlank(message = "description is required")
    @Size(min = 10, max = 500, message = "description should be between 10 and 500 characters long")
    private String description;

    @NotBlank(message = "location is required")
    @Size(min = 5, max = 100, message = "location should be between 5 and 100 characters long")
    private String location;

    @Positive(message = "salary must be positive")
    @NotNull(message = "salary is required")
    @Min(value = 10000, message = "salary should be at least 10,000")
    @Max(value = 1000000, message = "salary should not exceed 1,000,000")
    private Double salary;

    @Min(value = 0, message = "experience should not be non-negative")
    @NotNull(message = "experience is required")
    @Max(value = 40, message = "experience should not exceed 50 years")
    private int experienceRequired;

    @NotNull(message = "workmode is required")
    private WorkMode workMode;

    @NotNull(message = "jobstatus is required")
    private JobStatus jobStatus;
}
