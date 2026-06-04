package com.Medilabo_solutions.Evaluation_Service.service;

import com.Medilabo_solutions.Evaluation_Service.dto.PatientResponseDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "PATIENT-SERVICE")
public interface PatientClientService {
    @RequestMapping(method= RequestMethod.GET, value="/patients/id/{patientId}")
    PatientResponseDTO getPatientById(@PathVariable Long patientId);
}
