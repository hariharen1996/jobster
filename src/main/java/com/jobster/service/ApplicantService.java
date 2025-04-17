package com.jobster.service;

import java.time.LocalDateTime;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.jobster.dto.request.ApplicantRequest;
import com.jobster.dto.response.ApplicantResponse;
import com.jobster.entity.ApplicantProfile;
import com.jobster.entity.Role;
import com.jobster.entity.User;
import com.jobster.exception.JobExceptions;
import com.jobster.exception.ResourceNotFoundException;
import com.jobster.repository.ApplicantProfileRespository;
import com.jobster.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApplicantService {
    private final ApplicantProfileRespository applicantProfileRespository;
    private final UserRepository userRepository;

    @Transactional
    public ApplicantResponse createOrUpdateResponse(ApplicantRequest request){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("user not found"));

        if(user.getRole() != Role.APPLICANT){
            throw new JobExceptions("only applicants can create applicant profiles");
        }

        ApplicantProfile profile = applicantProfileRespository.findByUser(user).orElse(new ApplicantProfile());

        profile.setName(request.getName());
        profile.setEmail(request.getEmail());
        profile.setPhoneNumber(request.getPhoneNumber());
        profile.setAddress(request.getAddress());
        profile.setBio(request.getBio());
        profile.setSkills(request.getSkills());
        profile.setResumePath(request.getResumePath());
        profile.setProfilePicturePath(request.getProfilePicPath());
        profile.setExperience(request.getExperience());
        profile.setUser(user);

        if(profile.getId() == 0){
            profile.setCreatedAt(LocalDateTime.now());
        }

        ApplicantProfile savedProfile = applicantProfileRespository.save(profile);
        return addResponse(savedProfile);
    }

    public ApplicantResponse addResponse(ApplicantProfile profile){
        return new ApplicantResponse(
            profile.getId(),
            profile.getName(),
            profile.getEmail(),
            profile.getPhoneNumber(),
            profile.getAddress(),
            profile.getBio(),
            profile.getSkills(),
            profile.getResumePath(),
            profile.getProfilePicturePath(),
            profile.getExperience(),
            profile.getCreatedAt()
        );
    }
}
