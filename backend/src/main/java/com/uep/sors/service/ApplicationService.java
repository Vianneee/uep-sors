package com.uep.sors.service;

import com.uep.sors.dto.ApplicationSubmissionDTO;
import com.uep.sors.entity.Application;
import com.uep.sors.entity.ApplicationStatus;
import com.uep.sors.entity.RegistrationPeriod;
import com.uep.sors.exception.DuplicateApplicationException;
import com.uep.sors.exception.RegistrationClosedException;
import com.uep.sors.repository.ApplicationRepository;
import com.uep.sors.repository.RegistrationPeriodRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApplicationService {
    
    private final ApplicationRepository applicationRepository;
    private final RegistrationPeriodRepository registrationPeriodRepository;
    
    /**
     * Submit an application with registration window validation
     * FR-2: Allow users to submit membership registration forms
     * FR-3: Only allow submissions during registration window
     */
    @Transactional
    public Application submitApplication(ApplicationSubmissionDTO dto) {
        log.info("Submitting application for student: {} to organization: {}", 
                 dto.getStudentNumber(), dto.getOrganizationId());
        
        // Step 1: Verify registration period exists and is open
        RegistrationPeriod registrationPeriod = registrationPeriodRepository
                .findByOrganizationId(dto.getOrganizationId())
                .orElseThrow(() -> new RuntimeException("Registration period not found for organization"));
        
        // Step 2: Check if registration window is open
        if (!registrationPeriod.isOpen()) {
            log.warn("Registration closed for organization: {}", dto.getOrganizationId());
            throw new RegistrationClosedException(
                "Registration is currently closed. Registration period: " +
                registrationPeriod.getStartDate() + " to " + registrationPeriod.getEndDate()
            );
        }
        
        // Step 3: Check for duplicate active application (not rejected/withdrawn)
        Optional<Application> existingApplication = applicationRepository
                .findByOrganizationIdAndStudentNumberAndStatusIn(
                    dto.getOrganizationId(),
                    dto.getStudentNumber(),
                    java.util.List.of(ApplicationStatus.PENDING, ApplicationStatus.APPROVED)
                );
        
        if (existingApplication.isPresent()) {
            log.warn("Duplicate application attempt from student: {} for organization: {}", 
                    dto.getStudentNumber(), dto.getOrganizationId());
            throw new DuplicateApplicationException(
                "Student already has an active application for this organization"
            );
        }

        
        // Step 4: Create and save application
        Application application = Application.builder()
                .organizationId(dto.getOrganizationId())
                .studentNumber(dto.getStudentNumber())
                .fullName(dto.getFullName())
                .email(dto.getEmail())
                .contactNumber(dto.getContactNumber())
                .program(dto.getProgram())
                .yearLevel(dto.getYearLevel())
                .reasonForJoining(dto.getReasonForJoining())
                .skills(dto.getSkills())
                .previousExperience(dto.getPreviousExperience())
                .facebookLink(dto.getFacebookLink())
                .build();
        
        Application savedApplication = applicationRepository.save(application);
        log.info("Application submitted successfully with ID: {}", savedApplication.getId());
        
        return savedApplication;
    }
    
    /**
     * Get all applications for an organization
     * FR-6: System admins can see applicants for specific organization
     */
    public List<Application> getApplicationsByOrganization(Long organizationId) {
        return applicationRepository.findByOrganizationId(organizationId);
    }
    
    /**
     * Get application by ID
     */
    public Application getApplicationById(Long id) {
        return applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found"));
    }
    
    /**
     * Get all applications for a student
     */
    public List<Application> getApplicationsByStudent(String studentNumber) {
        return applicationRepository.findByStudentNumber(studentNumber);
    }
    
    /**
     * Delete an application
     * FR-7: System admins can delete applicants
     */
    @Transactional
    public void deleteApplication(Long id) {
        Application application = getApplicationById(id);
        applicationRepository.delete(application);
        log.info("Application deleted with ID: {}", id);
    }
    
    /**
     * Approve an application
     * FR-13/FR-15: Admin can approve applications with review notes
     */
    @Transactional
    public Application approveApplication(Long id, String reviewNotes) {
        Application application = getApplicationById(id);
        application.setStatus(ApplicationStatus.APPROVED);
        application.setReviewedAt(LocalDateTime.now());
        application.setReviewNotes(reviewNotes);
        
        Application updated = applicationRepository.save(application);
        log.info("Application {} approved with review notes", id);
        return updated;
    }
    
    /**
     * Reject an application
     * FR-13/FR-15: Admin can reject applications with review notes (5-year retention policy)
     */
    @Transactional
    public Application rejectApplication(Long id, String reviewNotes) {
        Application application = getApplicationById(id);
        application.setStatus(ApplicationStatus.REJECTED);
        application.setReviewedAt(LocalDateTime.now());
        application.setReviewNotes(reviewNotes);
        
        Application updated = applicationRepository.save(application);
        log.info("Application {} rejected with review notes. Data retained for 5-year policy", id);
        return updated;
    }
}
