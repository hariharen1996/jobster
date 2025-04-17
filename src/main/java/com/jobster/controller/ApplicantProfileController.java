package com.jobster.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobster.dto.request.ApplicantRequest;
import com.jobster.dto.response.ApplicantResponse;
import com.jobster.service.ApplicantService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/applicants")
public class ApplicantProfileController {
    private final ApplicantService applicantService;
    private final ObjectMapper objectMapper;

    @Value("${file.uploads}")
    private String uploadDirectory; 

    @PostMapping(value = "/create/applicant",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createOrUpdateApplicant(@RequestPart("resume") MultipartFile resume,@RequestPart("profilePic") MultipartFile profilePic,@RequestPart("applicant") String applicant){
        try{
            ApplicantRequest request = objectMapper.readValue(applicant, ApplicantRequest.class);
            String resumeFileName = saveByAbsPath(resume);
            String profilePicName = saveByAbsPath(profilePic);

            request.setResumePath(resumeFileName);
            request.setProfilePicPath(profilePicName);

            ApplicantResponse response = applicantService.createOrUpdateResponse(request);
            return ResponseEntity.ok(response);
            
        }catch(Exception e){
            return ResponseEntity.internalServerError().body(Map.of("error","file upload failed: " + e.getMessage()));
        }
    }

    @GetMapping("/download/{filename:.+}")
    public ResponseEntity<Resource> downloadFiles(@PathVariable String filename){
        try{
            Path filePath = Paths.get(uploadDirectory).resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if(resource.exists()){
                String contentType = Files.probeContentType(filePath);
                return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, contentType).header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"").body(resource);
            }else{
                return ResponseEntity.notFound().build();
            }
        }catch(Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    private String saveByAbsPath(MultipartFile multipartFile) throws IllegalStateException, IOException{
        String filename = multipartFile.getOriginalFilename();
        System.out.println(filename);
        String fileExtension = filename.substring(filename.lastIndexOf("."));
        System.out.println(fileExtension);
        String newFileName = UUID.randomUUID() + fileExtension;
        System.out.println(newFileName);

        Path uploadPath = Paths.get(uploadDirectory).toAbsolutePath().normalize();

        if(!Files.exists(uploadPath)){
            Files.createDirectories(uploadPath);
        }

        Path targetPath = uploadPath.resolve(newFileName);
        multipartFile.transferTo(targetPath);

        return newFileName;
    }

    
}
