package com.jobster.dto.request;


import com.jobster.entity.JobStatus;
import com.jobster.entity.WorkMode;
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
public class JobRequest {
    @NotBlank(message = "title is required")
    private String title;

    @NotBlank(message = "description is required")
    private String description;

    @NotBlank(message = "location is required")
    private String location;

    @Positive(message = "salary must be positive")
    @NotNull(message = "salary is required")
    private Double salary;

    @Min(value = 0, message = "experience should not be non-negative")
    @NotNull(message = "experience is required")
    private int experienceRequired;

    @NotNull(message = "workmode is required")
    private WorkMode workMode;

    @NotNull(message = "jobstatus is required")
    private JobStatus jobStatus;
}
