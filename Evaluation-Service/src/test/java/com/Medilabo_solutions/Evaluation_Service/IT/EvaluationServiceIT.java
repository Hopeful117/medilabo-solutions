package com.Medilabo_solutions.Evaluation_Service.IT;

import com.Medilabo_solutions.Evaluation_Service.dto.PatientNoteResponseDTO;
import com.Medilabo_solutions.Evaluation_Service.dto.PatientResponseDTO;
import com.Medilabo_solutions.Evaluation_Service.service.PatientClientService;
import com.Medilabo_solutions.Evaluation_Service.service.PatientHistoryClientService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class EvaluationServiceIT {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    private PatientClientService patientClient;

    @MockitoBean
    private PatientHistoryClientService historyClientService;

    @Test
    public void EvaluationPatientRisk_shouldReturnSuccess() throws Exception {
        String token = "Bearer some-valid-jwt-token";
        when(patientClient.getPatientById(1L))
                .thenReturn(new PatientResponseDTO(1,"Test", "None", LocalDate.parse("1991-02-21"),"M","2 rue bidon","111-222-333"));

        when(historyClientService.getPatientNotesByPatientId(1L))
                .thenReturn(List.of(
                        new PatientNoteResponseDTO("1",1L,"cholesterol", LocalDateTime.now()),
                        new PatientNoteResponseDTO("2",1L,"fumeur",LocalDateTime.now())


                ));

        mockMvc.perform(get("/evaluation/patient/id/"+"1")
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("BORDERLINE")));
    }




}
