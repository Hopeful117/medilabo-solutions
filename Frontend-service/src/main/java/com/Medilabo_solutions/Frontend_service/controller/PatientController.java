package com.Medilabo_solutions.Frontend_service.controller;

import com.Medilabo_solutions.Frontend_service.dto.PatientRequestDTO;
import com.Medilabo_solutions.Frontend_service.dto.PatientResponseDTO;
import com.Medilabo_solutions.Frontend_service.service.PatientClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class PatientController {
    private final PatientClientService patientClientService;

    @GetMapping("/patients")
    public String listPatients(Model model) {
        model.addAttribute("patients", patientClientService.getAllPatients());
        return "patients";

    }

    @GetMapping("/patients/id/{id}")
    public String getPatientById(@PathVariable Long id, Model model) {
        model.addAttribute("patient", patientClientService.getPatientById(id));
        return "patient-detail.html";
    }

    @GetMapping("/patients/lastname/{lastName}")
    public String getPatientsByLastName(@PathVariable String lastName, Model model) {
        model.addAttribute("patients", patientClientService.getPatientsByLastName(lastName));
        return "patients";
    }

    @GetMapping("/patients/lastname/{lastName}/firstname/{firstName}")
    public String getPatientByLastNameAndFirstName(@PathVariable String lastName, @PathVariable String firstName, Model model) {
        model.addAttribute("patient", patientClientService.getPatientByLastNameandFirstName(lastName, firstName));
        return "patient-detail.html";
    }

    @GetMapping("/patients/add")
    public String addPatientForm(Model model) {
        model.addAttribute("patient", new PatientResponseDTO());
        return "patient-form.html";
    }

    @PostMapping("/patients/add")
    public String createPatient(PatientRequestDTO patient) {
        patientClientService.createPatient(patient);
        return "redirect:/patients";
    }
    @GetMapping("/patients/edit/id/{id}")
    public String editPatientForm(@PathVariable Long id, Model model) {
        model.addAttribute("patient", patientClientService.getPatientById(id));
        return "patient-edit-form.html";
    }
    @PutMapping("/patients/id/{id}")
    public String updatePatient(@PathVariable Long id, PatientRequestDTO patient) {
        patientClientService.updatePatient(id, patient);
        return "redirect:/patients/id/" + id;
    }
    @DeleteMapping("/patients/id/{id}")
    public String deletePatient(@PathVariable Long id) {
        patientClientService.deletePatientById(id);
        return "redirect:/patients";
    }
}
