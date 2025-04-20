package com.jobster.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicantResponse {
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private String address;
    private String bio;
    private List<String> skills;
    private String resumeUrl;
    private String profilePic;
    private int experience;
    private LocalDateTime createdAt;
}
