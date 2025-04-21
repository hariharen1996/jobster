package com.jobster.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jobster.dto.request.JobRequest;
import com.jobster.dto.response.JobResponse;
import com.jobster.entity.JobStatus;
import com.jobster.entity.WorkMode;
import com.jobster.service.JobService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {
    private final JobService jobService;

    @PostMapping
    public ResponseEntity<JobResponse> createJob(@Valid @RequestBody JobRequest jobRequest) {
        return ResponseEntity.ok(jobService.createJob(jobRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobResponse> updateJob(@PathVariable Long id, @Valid @RequestBody JobRequest jobRequest) {
        return ResponseEntity.ok(jobService.updateJob(id, jobRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<JobResponse> deleteJob(@PathVariable Long id) {
        jobService.deleteJob(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<JobResponse>> getAllJobs() {
        List<JobResponse> jobResponses = jobService.getAllJobs();
        return ResponseEntity.ok(jobResponses);
    }

    @GetMapping("/employer")
    public ResponseEntity<List<JobResponse>> getJobsByEmployer() {
        return ResponseEntity.ok(jobService.getJobsByEmployer());
    }

    @GetMapping("/search")
    public ResponseEntity<Page<JobResponse>> searchJobs(@RequestParam(required = false) String title,@RequestParam(required = false) String location,
            @RequestParam(required = false) Double minSalary,@RequestParam(required = false) Double maxSalary,
            @RequestParam(required = false) Integer experience,@RequestParam(required = false) WorkMode workMode,
            @RequestParam(required = false) JobStatus status,@RequestParam(required = false) String freshness,@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,@RequestParam(defaultValue = "postedDate") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        Page<JobResponse> jobs = jobService.searchJobs(title,location,minSalary,maxSalary,experience,
                workMode,status,freshness,page,size,sortBy,direction);
        return ResponseEntity.ok(jobs);
    }

}
