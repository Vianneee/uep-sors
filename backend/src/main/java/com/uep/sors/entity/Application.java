package com.uep.sors.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "applications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Application {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long organizationId;
    
    @Column(nullable = false)
    private String studentNumber;
    
    @Column(nullable = false, length = 255)
    private String fullName;
    
    @Column(nullable = false, length = 255)
    private String email;
    
    @Column(nullable = false, length = 20)
    private String contactNumber;
    
    @Column(nullable = false, length = 255)
    private String program;
    
    @Column(nullable = false)
    private Integer yearLevel;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String reasonForJoining;
    
    @Column(columnDefinition = "TEXT")
    private String skills;
    
    @Column(columnDefinition = "TEXT")
    private String previousExperience;
    
    @Column(length = 500)
    private String facebookLink;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ApplicationStatus status = ApplicationStatus.PENDING;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime submittedAt;
    
    @Column
    private LocalDateTime reviewedAt;
    
    @Column(length = 500)
    private String reviewNotes;
    
    @PrePersist
    protected void onCreate() {
        submittedAt = LocalDateTime.now();
    }
}
