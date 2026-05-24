package com.uep.sors.dto;

import lombok.Data;

@Data
public class RegisterRequest {

    private String fullName;
    private String studentId;
    private Integer age;
    private String program;
    private Integer yearLevel;
    private String email;
    private String password;
    private String confirmPassword;
}