package com.Medilabo_solutions.Frontend_service.controller;

import com.Medilabo_solutions.Frontend_service.dto.PatientNoteRequestDTO;
import com.Medilabo_solutions.Frontend_service.dto.PatientRequestDTO;
import com.Medilabo_solutions.Frontend_service.dto.PatientResponseDTO;
import com.Medilabo_solutions.Frontend_service.exception.ApiException;
import com.Medilabo_solutions.Frontend_service.service.EvaluationClientService;
import com.Medilabo_solutions.Frontend_service.service.PatientClientService;
import com.Medilabo_solutions.Frontend_service.service.PatientHistoryClientService;
import feign.FeignException;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
/**
 * Controller responsible for handling patient-related web requests.
 * Provides endpoints for listing patients, viewing patient details, adding new patients, editing existing patients, and deleting patients.
 * Interacts with the PatientClientService to perform operations on patient data and returns appropriate views for each action.
 */
@Controller
@RequiredArgsConstructor
@Slf4j
public class PatientController {
    private final PatientClientService patientClientService;
    private final PatientHistoryClientService patientHistoryClientService;
    private final EvaluationClientService evaluationClientService;

    /**
     * Handles GET requests to list all patients.
     * Retrieves the list of patients from the PatientClientService and adds it to the model for rendering in the view.
     *
     * @param model   The Model object used to pass data to the view.
     * @return The name of the view template to render (patients.html).
     */
    @GetMapping("/patients")
    public String listPatients(Model model) {
        log.info("Accessing patient list page");

            model.addAttribute("patients", patientClientService.getAllPatients());
            return "patients";


    }
    /**
     * Handles GET requests to view details of a specific patient by ID.
     * Retrieves the patient details from the PatientClientService and adds it to the model for rendering in the view.
     *
     * @param id      The ID of the patient to retrieve.
     * @param model   The Model object used to pass data to the view.
     * @return The name of the view template to render (patient-detail.html).
     */
    @GetMapping("/patients/id/{id}")
    public String getPatientById(@PathVariable Long id, Model model) {
        log.info("Accessing patient detail page for patient ID: {}", id);

            model.addAttribute("patient", patientClientService.getPatientById(id));
            model.addAttribute("notes", patientHistoryClientService.getPatientNotesByPatientId(id));
            model.addAttribute("evaluation", evaluationClientService.getEvaluationByPatientId(id));

            return "patient-detail";


    }


    /**
     * Handles GET requests to display the form for adding a new patient.
     * Adds an empty PatientResponseDTO object to the model for form binding and returns the view for the patient form.
     *
     * @param model   The Model object used to pass data to the view.
     * @return The name of the view template to render (patient-form.html).
     */
    @GetMapping("/patients/add")
    public String addPatientForm(Model model) {
        log.info("Accessing patient creation form");

            model.addAttribute("patient", new PatientResponseDTO());

        return "patient-form";
    }
    /**
     * Handles POST requests to create a new patient.
     * Validates the submitted patient data and, if valid, calls the PatientClientService to create the patient.
     * If there are validation errors, it returns the patient form view with error messages.
     *
     * @param patient       The PatientRequestDTO object containing the submitted patient data.
     * @param bindingResult The BindingResult object used to check for validation errors.
     * @return A redirect to the list of patients if successful, or the patient form view if there are validation errors.
     */
    @PostMapping("/patients/add")
    public String createPatient(@Valid @ModelAttribute("patient") PatientRequestDTO patient, BindingResult bindingResult) {
        log.info("Processing patient creation form submission for patient: {} {}", patient.getFirstName(), patient.getLastName());
        if (bindingResult.hasErrors()) {
            log.warn("Patient creation form has validation errors: {}", bindingResult.getAllErrors());
            return "patient-form";
        }

            patientClientService.createPatient(patient);
            return "redirect:/patients";

    }
    /**
     * Handles GET requests to display the form for editing an existing patient.
     * Retrieves the patient details by ID and adds it to the model for form binding, then returns the view for the patient edit form.
     *
     * @param id      The ID of the patient to edit.
     * @param model   The Model object used to pass data to the view.
     * @return The name of the view template to render (patient-edit-form.html).
     */
    @GetMapping("/patients/edit/id/{id}")
    public String editPatientForm(@PathVariable Long id, Model model) {
        log.info("Accessing patient edit form for patient ID: {}", id);

            model.addAttribute("patient", patientClientService.getPatientById(id));
            return "patient-edit-form";

    }
    /**
     * Handles PUT requests to update an existing patient.
     * Validates the submitted patient data and, if valid, calls the PatientClientService to update the patient.
     * If there are validation errors, it returns the patient edit form view with error messages.
     *
     * @param id            The ID of the patient to update.
     * @param patient       The PatientRequestDTO object containing the submitted patient data.
     * @param bindingResult The BindingResult object used to check for validation errors.
     * @param model         The Model object used to pass data to the view.
     * @return A redirect to the patient details page if successful, or the patient edit form view if there are validation errors.
     */
    @PutMapping("/patients/edit/id/{id}")
    public String updatePatient(@PathVariable Long id,@Valid @ModelAttribute("patient") PatientRequestDTO patient,BindingResult bindingResult,Model model) {
        log.info("Processing patient update form submission for patient ID: {}", id);
        if (bindingResult.hasErrors()) {
            model.addAttribute("id", id);
            model.addAttribute("patient", patient);
            log.warn("Patient update form has validation errors for patient ID: {}: {}", id, bindingResult.getAllErrors());
            return "patient-edit-form";
        }

            patientClientService.updatePatient(id, patient);
            return "redirect:/patients/id/" + id;

    }
    /**
     * Handles GET requests to delete a patient by ID.
     * Calls the PatientClientService to delete the patient and then redirects to the list of patients.
     *
     * @param id      The ID of the patient to delete.
     * @return A redirect to the list of patients after deletion.
     */

    @GetMapping("/patients/notes/id/{id}")
    public String addNoteForm(@PathVariable Long id, Model model) {
        log.info("Accessing add note form for patient ID: {}", id);

            model.addAttribute("patient", patientClientService.getPatientById(id));
            model.addAttribute("note", new PatientNoteRequestDTO());
            return "patient-note-form";

    }

    @PostMapping("/patients/notes/id/{id}")
    public String addNote(
            @PathVariable Long id,
            @Valid @ModelAttribute("note") PatientNoteRequestDTO note,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {

            model.addAttribute("patient",
                    patientClientService.getPatientById(id));

            model.addAttribute("history",
                    patientHistoryClientService.getPatientNotesByPatientId(id));

            return "patient-detail";
        }

        patientHistoryClientService.addPatientNoteToPatient(id, note);

        return "redirect:/patients/id/" + id;
    }
}
