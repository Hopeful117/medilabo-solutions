package com.Medilabo_solutions.History_service.IT;

import com.Medilabo_solutions.History_service.dto.PatientNoteRequestDTO;
import com.Medilabo_solutions.History_service.model.PatientNote;
import com.Medilabo_solutions.History_service.repository.PatientNoteRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;


import java.time.LocalDateTime;

import static org.hamcrest.Matchers.containsString;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class HistoryServiceIT {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    PatientNoteRepository patientNoteRepository;

    @Autowired
    ObjectMapper mapper;

    @BeforeEach
    public void setUp(){
        patientNoteRepository.save(new PatientNote("1", 1L,"hello world !", LocalDateTime.now()));
    }

    @Test
    public void getAllNotes_shouldReturnOk() throws Exception {
        mockMvc.perform(get("/history/patient/id/"+"1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("hello world !")));
    }

    @Test
    public void addNoteShouldSucessfullyAddNoteToDB() throws Exception{
        PatientNoteRequestDTO requestDTO = new PatientNoteRequestDTO("foo bar");
        mockMvc.perform(post("/history/patient/id/"+"1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk());



    }


}
