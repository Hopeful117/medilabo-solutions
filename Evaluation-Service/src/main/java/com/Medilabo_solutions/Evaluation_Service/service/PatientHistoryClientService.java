package com.Medilabo_solutions.Evaluation_Service.service;

import com.Medilabo_solutions.Evaluation_Service.dto.PatientNoteResponseDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient("HISTORY-SERVICE")
public interface PatientHistoryClientService {
    @RequestMapping(method= RequestMethod.GET,value="/history/patient/id/{patientId}")
    List<PatientNoteResponseDTO> getPatientNotesByPatientId(@PathVariable("patientId") Long patientId);

}
