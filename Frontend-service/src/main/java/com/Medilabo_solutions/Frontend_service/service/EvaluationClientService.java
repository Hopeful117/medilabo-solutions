package com.Medilabo_solutions.Frontend_service.service;

import com.Medilabo_solutions.Frontend_service.dto.EvaluationResponseDTO;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name="EVALUATION-SERVICE")
public interface EvaluationClientService {
    @RequestMapping(method = RequestMethod.GET,value="/evaluation/patient/id/{patientId}")
    EvaluationResponseDTO getEvaluationByPatientId(@PathVariable long patientId);
}
