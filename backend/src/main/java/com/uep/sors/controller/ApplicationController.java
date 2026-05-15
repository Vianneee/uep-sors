package com.uep.sors.controller;

import com.uep.sors.dto.ApiResponse;
import com.uep.sors.dto.ApplicationSubmissionDTO;
import com.uep.sors.entity.Application;
import com.uep.sors.service.ApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/applications")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", allowedMethods = {"GET", "POST", "PUT", "DELETE"})
public class ApplicationController {
    
    private final ApplicationService applicationService;
    
    /**
     * POST /api/applications
     * Submit a new application
     * Validates registration window before accepting submission (FR-2, FR-3)
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Application>> submitApplication(
            @Valid @RequestBody ApplicationSubmissionDTO dto) {
        
        log.info("Received application submission request for organization: {}", dto.getOrganizationId());
        
        try {
            Application application = applicationService.submitApplication(dto);
            
            ApiResponse<Application> response = ApiResponse.builder()
                    .success(true)
                    .message("Application submitted successfully")
                    .data(application)
                    .build();
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception ex) {
            log.error("Error submitting application: {}", ex.getMessage());
            throw ex;
        }
    }
    
    /**
     * GET /api/applications/{id}
     * Get application by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Application>> getApplicationById(@PathVariable Long id) {
        try {
            Application application = applicationService.getApplicationById(id);
            
            ApiResponse<Application> response = ApiResponse.builder()
                    .success(true)
                    .message("Application retrieved successfully")
                    .data(application)
                    .build();
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException ex) {
            log.error("Error retrieving application: {}", ex.getMessage());
            throw ex;
        }
    }
    
    /**
     * GET /api/applications/organization/{organizationId}
     * Get all applications for an organization (FR-6)
     */
    @GetMapping("/organization/{organizationId}")
    public ResponseEntity<ApiResponse<List<Application>>> getApplicationsByOrganization(
            @PathVariable Long organizationId) {
        
        log.info("Fetching applications for organization: {}", organizationId);
        
        List<Application> applications = applicationService.getApplicationsByOrganization(organizationId);
        
        ApiResponse<List<Application>> response = ApiResponse.builder()
                .success(true)
                .message("Applications retrieved successfully")
                .data(applications)
                .build();
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * GET /api/applications/student/{studentNumber}
     * Get all applications for a student
     */
    @GetMapping("/student/{studentNumber}")
    public ResponseEntity<ApiResponse<List<Application>>> getApplicationsByStudent(
            @PathVariable String studentNumber) {
        
        log.info("Fetching applications for student: {}", studentNumber);
        
        List<Application> applications = applicationService.getApplicationsByStudent(studentNumber);
        
        ApiResponse<List<Application>> response = ApiResponse.builder()
                .success(true)
                .message("Applications retrieved successfully")
                .data(applications)
                .build();
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * DELETE /api/applications/{id}
     * Delete an application (FR-7)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> deleteApplication(@PathVariable Long id) {
        try {
            applicationService.deleteApplication(id);
            
            ApiResponse<Object> response = ApiResponse.builder()
                    .success(true)
                    .message("Application deleted successfully")
                    .build();
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException ex) {
            log.error("Error deleting application: {}", ex.getMessage());
            throw ex;
        }
    }
}
