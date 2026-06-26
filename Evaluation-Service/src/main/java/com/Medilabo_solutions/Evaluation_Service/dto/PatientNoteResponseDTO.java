package com.Medilabo_solutions.Evaluation_Service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientNoteResponseDTO {
    private String id;
    private Long patientId;
    private String note;
    private LocalDateTime createdAt;
}
