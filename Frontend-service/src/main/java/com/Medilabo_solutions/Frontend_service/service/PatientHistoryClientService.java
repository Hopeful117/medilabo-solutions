package com.Medilabo_solutions.Frontend_service.service;

import com.Medilabo_solutions.Frontend_service.dto.PatientNoteRequestDTO;
import com.Medilabo_solutions.Frontend_service.dto.PatientNoteResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(name="HISTORY-SERVICE")
public interface PatientHistoryClientService {

@RequestMapping(method= RequestMethod.GET, value="/history/patient/id/{patientId}")
List<PatientNoteResponseDTO> getPatientNotesByPatientId(@PathVariable long patientId);

@RequestMapping(method= RequestMethod.POST, value="/history/patient/id/{patientId}")
void addPatientNoteToPatient(@PathVariable long patientId, PatientNoteRequestDTO patientNoteRequest);
}
