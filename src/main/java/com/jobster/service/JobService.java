package com.jobster.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.jobster.dto.request.JobRequest;
import com.jobster.dto.response.EmployerResponse;
import com.jobster.dto.response.JobResponse;
import com.jobster.entity.EmployerProfile;
import com.jobster.entity.Job;
import com.jobster.entity.User;
import com.jobster.exception.JobExceptions;
import com.jobster.exception.ResourceNotFoundException;
import com.jobster.repository.EmployerProfileRepository;
import com.jobster.repository.JobRepository;
import com.jobster.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JobService {
    private final EmployerProfileRepository employerProfileRepository;
    private final EmployerService employerService;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;

    public JobResponse createJob(JobRequest jobRequest) {
        EmployerProfile employer = getCurrentEmployer();
        Job job = new Job();
        job.setTitle(jobRequest.getTitle());
        job.setDescription(jobRequest.getDescription());
        job.setLocation(jobRequest.getLocation());
        job.setSalary(jobRequest.getSalary());
        job.setExperienceRequired(jobRequest.getExperienceRequired());
        job.setWorkMode(jobRequest.getWorkMode());
        job.setJobStatus(jobRequest.getJobStatus());
        job.setEmployerProfile(employer);
        Job savedJob = jobRepository.save(job);
        return addResponse(savedJob);
    }

    public JobResponse updateJob(Long id,JobRequest jobRequest) {
        EmployerProfile employer = getCurrentEmployer();

        Job job = jobRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("job not found"));

        if(job.getEmployerProfile().getId() != employer.getId()){
            throw new JobExceptions("you can only update your own job");
        }

        job.setTitle(jobRequest.getTitle());
        job.setDescription(jobRequest.getDescription());
        job.setLocation(jobRequest.getLocation());
        job.setSalary(jobRequest.getSalary());
        job.setExperienceRequired(jobRequest.getExperienceRequired());
        job.setWorkMode(jobRequest.getWorkMode());
        job.setJobStatus(jobRequest.getJobStatus());
        
        Job updatedJob = jobRepository.save(job);
        return addResponse(updatedJob);
    }

    public void deleteJob(Long id){
        EmployerProfile employer = getCurrentEmployer();
        Job job = jobRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("job not found"));
        
        if(job.getEmployerProfile().getId() != employer.getId()){
            throw new JobExceptions("you can only delete your own job");
        }

        jobRepository.delete(job);
    }    

    public List<JobResponse> getAllJobs(){
        List<Job> jobs = jobRepository.findAll();
        return jobs.stream().map(this::addResponse).collect(Collectors.toList());
    }
    

    public JobResponse addResponse(Job job) {
        EmployerResponse employerResponse = employerService.addResponse(job.getEmployerProfile());
        return new JobResponse(
                job.getId(),
                job.getTitle(),
                job.getDescription(),
                job.getLocation(),
                job.getSalary(),
                job.getExperienceRequired(),
                job.getWorkMode(),
                job.getJobStatus(),
                job.getPostedDate(),
                employerResponse);
    }

    private EmployerProfile getCurrentEmployer() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("user not found"));
        return employerProfileRepository.findByUser(user).orElseThrow(() -> new JobExceptions("employer profile not found"));
    }
}
