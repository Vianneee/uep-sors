package com.uep.sors.dto;

import lombok.Data;

@Data
public class CreatePioRequest {
    private String fullName;
    private String studentId;
    private Integer age;
    private String program;
    private Integer yearLevel;
    private String email;
    private String password;
    private Long organizationId;
}