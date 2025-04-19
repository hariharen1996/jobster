package com.jobster.dto.response;

import java.time.LocalDateTime;

import com.jobster.entity.JobStatus;
import com.jobster.entity.WorkMode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobResponse {
    private Long id;
    private String title;
    private String description;
    private String location;
    private Double salary;
    private int experienceRequired;
    private WorkMode workMode;
    private JobStatus jobStatus;
    private LocalDateTime postedDate;
    private EmployerResponse employerResponse;
}
