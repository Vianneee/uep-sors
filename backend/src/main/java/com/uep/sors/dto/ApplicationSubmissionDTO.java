package com.uep.sors.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationSubmissionDTO {
    
    @NotNull(message = "Organization ID is required")
    private Long organizationId;
    
    @NotBlank(message = "Student number is required")
    @Pattern(regexp = "\\d{6}", message = "Student number must be 6 digits")
    private String studentNumber;
    
    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 255, message = "Full name must be between 2 and 255 characters")
    private String fullName;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;
    
    @NotBlank(message = "Contact number is required")
    @Pattern(regexp = "\\d{11}", message = "Contact number must be 11 digits")
    private String contactNumber;
    
    @NotBlank(message = "Program/Course is required")
    @Size(min = 2, max = 255, message = "Program must be between 2 and 255 characters")
    private String program;
    
    @NotNull(message = "Year level is required")
    @Min(value = 1, message = "Year level must be at least 1")
    @Max(value = 4, message = "Year level must be at most 4")
    private Integer yearLevel;
    
    @NotBlank(message = "Reason for joining is required")
    @Size(min = 10, max = 2000, message = "Reason must be between 10 and 2000 characters")
    private String reasonForJoining;
    
    @NotBlank(message = "Skills are required")
    @Size(min = 10, max = 2000, message = "Skills must be between 10 and 2000 characters")
    private String skills;
    
    @Size(max = 2000, message = "Previous experience must not exceed 2000 characters")
    private String previousExperience;
    
    @Size(max = 500, message = "Facebook link must not exceed 500 characters")
    private String facebookLink;
}
