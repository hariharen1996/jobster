package com.jobster.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jobster.entity.EmployerProfile;
import com.jobster.entity.User;

@Repository
public interface EmployerProfileRepository extends JpaRepository<EmployerProfile,Integer> {
    Optional<EmployerProfile> findByUser(User user);
}
