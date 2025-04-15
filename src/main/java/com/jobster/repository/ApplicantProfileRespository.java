package com.jobster.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jobster.entity.ApplicantProfile;
import com.jobster.entity.User;

@Repository
public interface ApplicantProfileRespository extends JpaRepository<ApplicantProfile,Integer> {
    Optional<ApplicantProfile> findByUser(User user);
}
