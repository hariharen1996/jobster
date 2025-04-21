package com.jobster.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.jobster.dto.request.JobRequest;
import com.jobster.dto.response.EmployerResponse;
import com.jobster.dto.response.JobResponse;
import com.jobster.entity.EmployerProfile;
import com.jobster.entity.Job;
import com.jobster.entity.JobStatus;
import com.jobster.entity.User;
import com.jobster.entity.WorkMode;
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

    public JobResponse updateJob(Long id, JobRequest jobRequest) {
        EmployerProfile employer = getCurrentEmployer();

        Job job = jobRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("job not found"));

        if (job.getEmployerProfile().getId() != employer.getId()) {
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

    public void deleteJob(Long id) {
        EmployerProfile employer = getCurrentEmployer();
        Job job = jobRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("job not found"));

        if (job.getEmployerProfile().getId() != employer.getId()) {
            throw new JobExceptions("you can only delete your own job");
        }

        jobRepository.delete(job);
    }

    public List<JobResponse> getAllJobs() {
        List<Job> jobs = jobRepository.findAll();
        return jobs.stream().map(this::addResponse).collect(Collectors.toList());
    }

    public List<JobResponse> getJobsByEmployer() {
        EmployerProfile employer = getCurrentEmployer();
        List<Job> jobs = jobRepository.findByEmployerProfile(employer);
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
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("user not found"));
        return employerProfileRepository.findByUser(user)
                .orElseThrow(() -> new JobExceptions("employer profile not found"));
    }

    public Page<JobResponse> searchJobs(String title,String location,Double minSalary,Double maxSalary,Integer maxExperience,WorkMode workMode,
            JobStatus status,String freshness,int page,int size,String sortBy,String direction) {

        LocalDateTime dateThreshold = calculateDateThreshold(freshness);
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Specification<Job> specification = Specification.where(null);

        if (StringUtils.hasText(title)) {
            specification = specification.and((root, query, cb) -> cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
        }

        if (StringUtils.hasText(location)) {
            specification = specification.and(
                    (root, query, cb) -> cb.like(cb.lower(root.get("location")), "%" + location.toLowerCase() + "%"));
        }

        if (minSalary != null) {
            specification = specification.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("salary"), minSalary));
        }

        if (maxSalary != null) {
            specification = specification.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("salary"), maxSalary));
        }

        if (maxExperience != null) {
            specification = specification.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("experienceRequired"), maxExperience));
        }

        if (workMode != null) {
            specification = specification.and((root, query, cb) -> cb.equal(root.get("workMode"), workMode));
        }

        if (status != null) {
            specification = specification.and((root, query, cb) -> cb.equal(root.get("status"), status));
        }

        if (dateThreshold != null) {
            specification = specification.and((root, query, cb) -> cb.greaterThan(root.get("postedDate"), dateThreshold));
        }

        return jobRepository.findAll(specification, pageable).map(this::addResponse);
    }

    private LocalDateTime calculateDateThreshold(String freshness) {
        if (freshness == null)
            return null;

        return switch (freshness.toLowerCase()) {
            case "just now" -> LocalDateTime.now().minusMinutes(5);
            case "last 1 day" -> LocalDateTime.now().minusDays(1);
            case "last 3 days" -> LocalDateTime.now().minusDays(3);
            case "last 7 days" -> LocalDateTime.now().minusDays(7);
            case "last 15 days" -> LocalDateTime.now().minusDays(15);
            case "last 30 days" -> LocalDateTime.now().minusDays(30);
            default -> null;
        };
    }
}
