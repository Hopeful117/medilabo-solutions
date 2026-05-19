package com.Medilabo_solutions.Evaluation_Service.service;

import com.Medilabo_solutions.Evaluation_Service.dto.EvaluationResponseDTO;
import com.Medilabo_solutions.Evaluation_Service.dto.PatientNoteResponseDTO;
import com.Medilabo_solutions.Evaluation_Service.helper.RiskLevels;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EvaluationServiceImpl implements EvaluationService {
    private final PatientHistoryClientService patientHistoryClientService;
    private final PatientClientService patientClientService;

    @Override
    public EvaluationResponseDTO evaluatePatientRisk(Long patientId, String token   ) {
        var patient = patientClientService.getPatientById(patientId,token);
        var notes = patientHistoryClientService.getPatientNotesByPatientId(patientId,token);
        int age = calculateAge(patient.getDateOfBirth());
        int triggerCount = countTriggers(notes.stream().map(PatientNoteResponseDTO::getNote).toList());

        RiskLevels risk = age > 30
            ? evaluateRiskAdults(triggerCount) 
            : evaluateRiskYoungAdults(triggerCount, patient.getGender());
        
        return new EvaluationResponseDTO(patientId, risk);
    }

    private RiskLevels evaluateRiskAdults(int triggerCount) {
        if (triggerCount == 0) return RiskLevels.NONE;
        if (triggerCount <= 5) return RiskLevels.BORDERLINE;
        if (triggerCount <= 7) return RiskLevels.IN_DANGER;
        return RiskLevels.EARLY_ONSET;
    }

    private RiskLevels evaluateRiskYoungAdults(int triggerCount, String gender) {
        boolean isMale = gender.equalsIgnoreCase("M");
        int maleThreshold = isMale ? 3 : 4;
        int earlyOnsetThreshold = isMale ? 5 : 7;

        if (triggerCount < maleThreshold) return RiskLevels.NONE;
        if (triggerCount < earlyOnsetThreshold) return RiskLevels.IN_DANGER;
        return RiskLevels.EARLY_ONSET;
    }

    private static final List<String> TRIGGERS = List.of(
            "hémoglobine a1c",
            "microalbumine",
            "taille",
            "poids",
            "fumeur",
            "fumeuse",
            "anormal",
            "cholestérol",
            "vertiges",
            "rechute",
            "réaction",
            "anticorps"
    );

    private int calculateAge(LocalDate birthDate) {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }
    private int countTriggers(List<String> notes) {
        int count = 0;
        for (String note : notes) {
            for (String trigger : TRIGGERS) {
                if(note.toLowerCase().contains("bien")){
                    continue;
                }
                if (note.toLowerCase().contains(trigger)) {
                    count++;

                }
            }
        }
        return count;
    }
}
