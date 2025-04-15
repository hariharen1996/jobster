package com.jobster.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployerResponse {
    private int id;
    private String employerName;
    private String companyName;
    private String companyLocation;
    private String email;
    private String companyLogo;
    private String employerProfilePic;
    private Map<String,List<String>> companyTechStack;    
    private LocalDateTime createdAt;
}
