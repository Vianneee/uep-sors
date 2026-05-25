package com.uep.sors.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationReviewDTO {
    private String status; // APPROVED or REJECTED
    private String reviewNotes;
}
