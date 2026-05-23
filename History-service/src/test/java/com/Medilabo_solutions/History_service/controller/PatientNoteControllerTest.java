package com.Medilabo_solutions.History_service.controller;

import com.Medilabo_solutions.History_service.config.GlobalExceptionHandler;
import com.Medilabo_solutions.History_service.dto.PatientNoteRequestDTO;
import com.Medilabo_solutions.History_service.dto.PatientNoteResponseDTO;
import com.Medilabo_solutions.History_service.exception.NoteNotFoundException;
import com.Medilabo_solutions.History_service.service.PatientNoteService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = PatientNoteController.class)
@Import(GlobalExceptionHandler.class)
public class PatientNoteControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private PatientNoteService patientNoteService;

        /**
        * Test cases:
        * POST /patients/{patientId}/notes -> 201 + created note
        * POST /patients/{patientId}/notes -> 400 if validation error
        * GET /patients/{patientId}/notes -> 200 + list of notes
        * GET /patients/{patientId}/notes -> 404 if patient not found
        */




    @Test
    @DisplayName("GET /history/patient/id/{patientId} - Success")
    void getHistory_success() throws Exception {
        Long patientId = 3L;
        PatientNoteResponseDTO a = PatientNoteResponseDTO.builder().id("a").patientId(patientId).note("one").createdAt(LocalDateTime.now()).build();
        PatientNoteResponseDTO b = PatientNoteResponseDTO.builder().id("b").patientId(patientId).note("two").createdAt(LocalDateTime.now()).build();

        when(patientNoteService.getHistory(patientId)).thenReturn(List.of(a, b));

        mockMvc.perform(get("/history/patient/id/{patientId}", patientId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value("a"));
    }

    @Test
    @DisplayName("POST /history/patient/id/{patientId} - Success")
    void postAddNote_success() throws Exception {
        Long patientId = 5L;
        String requestJson = "{\"content\": \"Test from MVC POST\"}";

        PatientNoteResponseDTO responseDto = PatientNoteResponseDTO.builder()
                .id("id-post")
                .patientId(patientId)
                .note("Test from MVC POST")
                .createdAt(LocalDateTime.now())
                .build();

        when(patientNoteService.addNote(eq(patientId), any())).thenReturn(responseDto);

        mockMvc.perform(post("/history/patient/id/{patientId}", patientId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("id-post"))
                .andExpect(jsonPath("$.note").value("Test from MVC POST"));
    }

    @Test
    @DisplayName("POST /history/patient/id/{patientId} - Validation Failure")
    void postAddNote_validationFails_shouldReturnBadRequest() throws Exception {
        Long patientId = 6L;
        String requestJson = "{\"content\": \"\"}"; // empty content -> NotBlank violation

        mockMvc.perform(post("/history/patient/id/{patientId}", patientId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value(containsString("Validation failed")));
    }

    @Test
    @DisplayName("GET /history/patient/id/{patientId} - Not Found")
    void getHistory_whenNotFound_shouldReturn404() throws Exception {
        Long patientId = 4L;
        when(patientNoteService.getHistory(patientId)).thenThrow(new NoteNotFoundException("No notes for patient"));

        mockMvc.perform(get("/history/patient/id/{patientId}", patientId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value(containsString("No notes for patient")));
    }

}


