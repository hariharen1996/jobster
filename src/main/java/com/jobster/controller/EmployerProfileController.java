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
import com.jobster.dto.request.EmployerRequest;
import com.jobster.dto.response.EmployerResponse;
import com.jobster.service.EmployerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/employer")
public class EmployerProfileController {
    private final EmployerService employerService;
    private final ObjectMapper objectMapper;

    @Value("${file.uploads}")
    private String uploadDirectory; 

    @PostMapping(value = "/create/employer",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createOrUpdateEmployer(@RequestPart("companyLogo") MultipartFile companyLogo,@RequestPart("employerProfilePic") MultipartFile employerProfilePic,@RequestPart("employer") String employer){
        try{
            EmployerRequest request = objectMapper.readValue(employer, EmployerRequest.class);
            String companyLogoName = saveByAbsPath(companyLogo);
            String profilePicName = saveByAbsPath(employerProfilePic);

            request.setCompanyLogo(companyLogoName);
            request.setEmployerProfilePic(profilePicName);

            EmployerResponse response = employerService.createOrUpdateResponse(request);
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
