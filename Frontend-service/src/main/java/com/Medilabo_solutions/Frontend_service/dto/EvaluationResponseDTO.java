package com.Medilabo_solutions.Frontend_service.dto;

import com.Medilabo_solutions.Frontend_service.helper.RiskLevels;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EvaluationResponseDTO {
    private long id;
    private RiskLevels riskLevel;

}
