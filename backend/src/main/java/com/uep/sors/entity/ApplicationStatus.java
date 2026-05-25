package com.uep.sors.entity;

public enum ApplicationStatus {
    PENDING("Application submitted, awaiting review"),
    APPROVED("Application has been approved"),
    REJECTED("Application has been rejected"),
    WITHDRAWN("Application has been withdrawn");
    
    private final String description;
    
    ApplicationStatus(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}
