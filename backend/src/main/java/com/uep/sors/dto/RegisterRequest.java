package com.uep.sors.dto;

import com.uep.sors.entity.Role;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank
    private String username;

    @NotBlank
    @Pattern(
        regexp = "^\\d{6}$",
        message = "Student ID must be exactly 6 digits"
    )
    private String studentId;

    @NotBlank
    private String password; // birthday e.g. "2004-05-18"

    private Role role;
}