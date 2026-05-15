package com.Medilabo_solutions.Frontend_service.controller;

import com.Medilabo_solutions.Frontend_service.dto.PatientRequestDTO;
import com.Medilabo_solutions.Frontend_service.dto.PatientResponseDTO;
import com.Medilabo_solutions.Frontend_service.service.PatientClientService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class PatientController {
    private final PatientClientService patientClientService;


    @GetMapping("/patients")
    public String listPatients(Model model, HttpSession session) {
        model.addAttribute("patients", patientClientService.getAllPatients(session));
        return "patients";

    }

    @GetMapping("/patients/id/{id}")
    public String getPatientById(@PathVariable Long id, Model model,HttpSession session) {
        model.addAttribute("patient", patientClientService.getPatientById(id,session));
        return "patient-detail.html";
    }

    @GetMapping("/patients/lastname/{lastName}")
    public String getPatientsByLastName(@PathVariable String lastName, Model model,HttpSession session) {
        model.addAttribute("patients", patientClientService.getPatientsByLastName(lastName,session));
        return "patients";
    }

    @GetMapping("/patients/lastname/{lastName}/firstname/{firstName}")
    public String getPatientByLastNameAndFirstName(@PathVariable String lastName, @PathVariable String firstName, Model model,HttpSession session) {
        model.addAttribute("patient", patientClientService.getPatientByLastNameAndFirstName(lastName, firstName,session));
        return "patient-detail.html";
    }

    @GetMapping("/patients/add")
    public String addPatientForm(Model model,HttpSession session) {
        model.addAttribute("patient", new PatientResponseDTO());
        return "patient-form.html";
    }

    @PostMapping("/patients/add")
    public String createPatient(PatientRequestDTO patient,HttpSession session) {
        patientClientService.createPatient(patient,session);
        return "redirect:/patients";
    }
    @GetMapping("/patients/edit/id/{id}")
    public String editPatientForm(@PathVariable Long id, Model model,HttpSession session) {
        model.addAttribute("patient", patientClientService.getPatientById(id,session));
        return "patient-edit-form.html";
    }
    @PutMapping("/patients/edit/id/{id}")
    public String updatePatient(@PathVariable Long id, PatientRequestDTO patient,HttpSession session) {
        patientClientService.updatePatient(id, patient,session);
        return "redirect:/patients/id/" + id;
    }
    @GetMapping("/patients/delete/id/{id}")
    public String deletePatient(@PathVariable Long id,HttpSession session) {
        patientClientService.deletePatientById(id,session);
        return "redirect:/patients";
    }
}
