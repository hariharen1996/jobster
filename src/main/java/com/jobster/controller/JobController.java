package com.jobster.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jobster.dto.request.JobRequest;
import com.jobster.dto.response.JobResponse;
import com.jobster.service.JobService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {
    private final JobService jobService;

    @PostMapping
    public ResponseEntity<JobResponse> createJob(@Valid @RequestBody JobRequest jobRequest){
        return ResponseEntity.ok(jobService.createJob(jobRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobResponse> updateJob(@PathVariable Long id,@Valid @RequestBody JobRequest jobRequest){
        return ResponseEntity.ok(jobService.updateJob(id,jobRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<JobResponse> deleteJob(@PathVariable Long id){
        jobService.deleteJob(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<JobResponse>> getAllJobs(){
        List<JobResponse> jobResponses = jobService.getAllJobs();
        return ResponseEntity.ok(jobResponses);
    }
}
