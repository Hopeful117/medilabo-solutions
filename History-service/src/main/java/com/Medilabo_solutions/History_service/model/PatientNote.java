package com.Medilabo_solutions.History_service.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "patient_notes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientNote {
    @Id
    private String id;
    private Long patientId;
    private String note;
    private LocalDateTime createdAt;
}
