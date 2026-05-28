package com.Medilabo_solutions.Frontend_service.service;

import com.Medilabo_solutions.Frontend_service.dto.PatientRequestDTO;
import com.Medilabo_solutions.Frontend_service.dto.PatientResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient("PATIENT-SERVICE")
public interface PatientClientService {

    @RequestMapping(method = RequestMethod.GET, value = "/patients/all")
    List<PatientResponseDTO> getAllPatients();

    @RequestMapping(method = RequestMethod.GET, value = "/patients/id/{id}")
    PatientResponseDTO getPatientById(@PathVariable("id") Long id);

    @RequestMapping(method = RequestMethod.POST, value = "/patients/add")
    void createPatient(PatientRequestDTO patientRequest);

    @RequestMapping(method = RequestMethod.PUT, value = "/patients/update/id/{id}")
    void updatePatient(@PathVariable("id") long id, PatientRequestDTO patientRequest);

}

