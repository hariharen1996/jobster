package com.jobster.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.jobster.entity.EmployerProfile;
import com.jobster.entity.Job;

@Repository
public interface JobRepository extends JpaRepository<Job,Long>,JpaSpecificationExecutor<Job> {
    List<Job> findByEmployerProfile(EmployerProfile employerProfile);
}
