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
@CrossOrigin(origins = "*")
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping
    public ResponseEntity<ApiResponse<Application>> submitApplication(
            @Valid @RequestBody ApplicationSubmissionDTO dto) {
        log.info("Received application submission for organization: {}", dto.getOrganizationId());
        Application application = applicationService.submitApplication(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Application submitted successfully", application));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Application>> getApplicationById(@PathVariable Long id) {
        Application application = applicationService.getApplicationById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Application retrieved successfully", application));
    }

    @GetMapping("/organization/{organizationId}")
    public ResponseEntity<ApiResponse<List<Application>>> getApplicationsByOrganization(
            @PathVariable Long organizationId) {
        log.info("Fetching applications for organization: {}", organizationId);
        List<Application> applications = applicationService.getApplicationsByOrganization(organizationId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Applications retrieved successfully", applications));
    }

    @GetMapping("/student/{studentNumber}")
    public ResponseEntity<ApiResponse<List<Application>>> getApplicationsByStudent(
            @PathVariable String studentNumber) {
        log.info("Fetching applications for student: {}", studentNumber);
        List<Application> applications = applicationService.getApplicationsByStudent(studentNumber);
        return ResponseEntity.ok(new ApiResponse<>(true, "Applications retrieved successfully", applications));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> deleteApplication(@PathVariable Long id) {
        applicationService.deleteApplication(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Application deleted successfully"));
    }
}
