package com.Medilabo_solutions.Patient_service.controller;

import com.Medilabo_solutions.Patient_service.dto.PatientRequestDTO;
import com.Medilabo_solutions.Patient_service.exception.DuplicateResourceException;
import com.Medilabo_solutions.Patient_service.exception.InvalidDateException;
import com.Medilabo_solutions.Patient_service.exception.ResourceNotFoundException;
import com.Medilabo_solutions.Patient_service.model.Patient;
import com.Medilabo_solutions.Patient_service.service.PatientService;
import com.Medilabo_solutions.Patient_service.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = PatientController.class)
@Import(GlobalExceptionHandler.class)
@DisplayName("PatientController - WebMvc Tests")

public class PatientControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private PatientService patientService;

    /**
     * Test cases:
     * GET /patients/all -> 200 + list
     * GET /patients/id/{id} -> 200 + patient
     * GET /patients/id/{id} -> 404 if not found
     * POST /patients/add -> 201 + created patient
     * POST /patients/add -> 400 if validation error
     * POST /patients/add -> 409 if duplicate
     * PUT /patients/update/{id} -> 200 + updated patient
     * PUT /patients/update/{id} -> 404 if not found
     * PUT /patients/update/{id} -> 400 if invalid date
     * DELETE /patients/delete/{id} -> 204 no content
     * DELETE /patients/delete/{id} -> 404 if not found
     */
	@Test
	@DisplayName("GET /patients/all - success")
	void getAllPatients_success() throws Exception {
		Patient p1 = new Patient();
		p1.setId(1);
		p1.setFirstName("John");
		p1.setLastName("Doe");
		p1.setDateOfBirth(LocalDate.of(1990,1,1));

		when(patientService.getAllPatients()).thenReturn(List.of(p1));

		mockMvc.perform(get("/patients/all"))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[0].firstName").value("John"));
	}

	@Test
	@DisplayName("GET /patients/id/{id} - success")
	void getPatientById_success() throws Exception {
		Patient p = new Patient();
		p.setId(1);
		p.setFirstName("John");
		p.setLastName("Doe");
		p.setDateOfBirth(LocalDate.of(1990,1,1));

		when(patientService.getPatientById(1L)).thenReturn(p);

		mockMvc.perform(get("/patients/id/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.firstName").value("John"))
				.andExpect(jsonPath("$.lastName").value("Doe"));
	}

	@Test
	@DisplayName("GET /patients/id/{id} - not found -> 404")
	void getPatientById_notFound() throws Exception {
		when(patientService.getPatientById(999L)).thenThrow(new ResourceNotFoundException("Not found"));

		mockMvc.perform(get("/patients/id/999"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.status").value(404));
	}

	@Test
	@DisplayName("POST /patients/add - success -> 201")
	void createPatient_success() throws Exception {
		String json = "{\"firstName\":\"Alice\",\"lastName\":\"Wonder\",\"dateOfBirth\":\"1995-07-04\",\"gender\":\"F\"}";

		Patient created = new Patient();
		created.setId(5);
		created.setFirstName("Alice");
		created.setLastName("Wonder");
		created.setDateOfBirth(LocalDate.of(1995,7,4));

		when(patientService.createPatient(any(Patient.class))).thenReturn(created);

		mockMvc.perform(post("/patients/add")
						.contentType(MediaType.APPLICATION_JSON)
						.content(json))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").value(5))
				.andExpect(jsonPath("$.firstName").value("Alice"));
	}

	@Test
	@DisplayName("POST /patients/add - validation error -> 400")
	void createPatient_validationError() throws Exception {
		String json = "{\"lastName\":\"X\",\"dateOfBirth\":\"1990-01-01\",\"gender\":\"M\"}";

		mockMvc.perform(post("/patients/add")

						.contentType(MediaType.APPLICATION_JSON)
						.content(json))
				.andExpect(status().isBadRequest());

	}

	@Test
	@DisplayName("POST /patients/add - duplicate -> 409")
	void createPatient_duplicate() throws Exception {
		String json = "{\"firstName\":\"Bob\",\"lastName\":\"Smith\",\"dateOfBirth\":\"1980-01-01\",\"gender\":\"M\"}";

		when(patientService.createPatient(any(Patient.class))).thenThrow(new DuplicateResourceException("Dup"));

		mockMvc.perform(post("/patients/add")
						.contentType(MediaType.APPLICATION_JSON)
						.content(json))
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.status").value(409));
	}

	@Test
	@DisplayName("PUT /patients/update/id/{id} - success -> 200")
	void updatePatient_success() throws Exception {
		String json = "{\"firstName\":\"Sam\",\"lastName\":\"Green\",\"dateOfBirth\":\"1990-02-02\",\"gender\":\"M\"}";
		Patient updated = new Patient();
		updated.setId(2);
		updated.setFirstName("Sam");
		updated.setLastName("Green");
		updated.setDateOfBirth(LocalDate.of(1990,2,2));

		when(patientService.updatePatient(org.mockito.ArgumentMatchers.eq(2L), any(Patient.class))).thenReturn(updated);

		mockMvc.perform(put("/patients/update/id/2")

						.contentType(MediaType.APPLICATION_JSON)
						.content(json))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(2))
				.andExpect(jsonPath("$.firstName").value("Sam"));
	}

	@Test
	@DisplayName("PUT /patients/update/id/{id} - not found -> 404")
	void updatePatient_notFound() throws Exception {
		String json = "{\"firstName\":\"X\",\"lastName\":\"Y\",\"dateOfBirth\":\"1990-01-01\",\"gender\":\"M\"}";

		when(patientService.updatePatient(org.mockito.ArgumentMatchers.eq(999L), any(Patient.class))).thenThrow(new ResourceNotFoundException("Not found"));

		mockMvc.perform(put("/patients/update/id/999")

						.contentType(MediaType.APPLICATION_JSON)
						.content(json))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.status").value(404));
	}






}

