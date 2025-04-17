package com.jobster.service;

import java.time.LocalDateTime;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jobster.dto.request.EmployerRequest;
import com.jobster.dto.response.EmployerResponse;
import com.jobster.entity.EmployerProfile;
import com.jobster.entity.Role;
import com.jobster.entity.User;
import com.jobster.exception.JobExceptions;
import com.jobster.exception.ResourceNotFoundException;
import com.jobster.repository.EmployerProfileRepository;
import com.jobster.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployerService {
    private final EmployerProfileRepository employerProfileRepository;
    private final UserRepository userRepository;

    @Transactional
    public EmployerResponse createOrUpdateResponse(EmployerRequest request){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("user not found"));

        if(user.getRole() != Role.EMPLOYER){
            throw new JobExceptions("only employer can create employer profiles");
        }

        EmployerProfile profile = employerProfileRepository.findByUser(user).orElse(new EmployerProfile());

        profile.setEmployerName(request.getEmployerName());
        profile.setCompanyName(request.getCompanyName());
        profile.setCompanyLocation(request.getCompanyLocation());
        profile.setEmail(request.getEmail());
        profile.setCompanyLogo(request.getCompanyLogo());
        profile.setEmployerProfilePic(request.getEmployerProfilePic());
        profile.setCompanyTechStack(request.getCompanyTechStack());
        profile.setUser(user);

        if(profile.getId() == 0){
            profile.setCreatedAt(LocalDateTime.now());
        }

        EmployerProfile savedProfile = employerProfileRepository.save(profile);
        return addResponse(savedProfile);
    }

    public EmployerResponse addResponse(EmployerProfile profile){
        return new EmployerResponse(
            profile.getId(),
            profile.getEmployerName(),
            profile.getCompanyName(),
            profile.getCompanyLocation(),
            profile.getEmail(),
            profile.getCompanyLogo(),
            profile.getEmployerProfilePic(),
            profile.getCompanyTechStack(),
            profile.getCreatedAt()
        );
    }
}

