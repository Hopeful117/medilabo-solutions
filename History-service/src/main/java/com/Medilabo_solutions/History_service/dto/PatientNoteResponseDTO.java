package com.Medilabo_solutions.History_service.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class PatientNoteResponseDTO {
    private String id;
    private Long patientId;
    private String note;
    private LocalDateTime createdAt;
}
