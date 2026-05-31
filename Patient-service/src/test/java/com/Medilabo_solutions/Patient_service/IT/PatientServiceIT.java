package com.Medilabo_solutions.Patient_service.IT;

import com.Medilabo_solutions.Patient_service.dto.PatientRequestDTO;
import com.Medilabo_solutions.Patient_service.model.Patient;
import com.Medilabo_solutions.Patient_service.repository.PatientRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.hamcrest.Matchers.containsString;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class PatientServiceIT {

	@Autowired
	private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PatientRepository patientRepository;



    @BeforeEach
    public void setUp(){
        patientRepository.save(new Patient("TestNone","Test",LocalDate.parse("1966-12-31"), Patient.Gender.F));
        patientRepository.save(new Patient("TestBorderline","Test2",LocalDate.parse("1996-12-31"), Patient.Gender.M));


    }



	@Test
	public void getAllPatients_shouldReturnPreloadedPatients() throws Exception {

		mockMvc.perform(get("/patients/all"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("TestNone")))
				.andExpect(content().string(containsString("TestBorderline")));
	}

	@Test
	public void createPatient_shouldAddPatientToDb() throws Exception {

        PatientRequestDTO requestDTO= new PatientRequestDTO(
                "test",
                "2",
                LocalDate.of(1991,02,21),
                "M"

        );

        mockMvc.perform(post("/patients/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                        .andExpect(status().isCreated());


    }

    @Test
    public void getPatient_shouldReturnPatientFromDb() throws Exception {
        Patient testPatient = new Patient("testgetid","it",LocalDate.parse("1992-05-21"), Patient.Gender.M);
        patientRepository.save(testPatient);
        int id = testPatient.getId();

        mockMvc.perform(get("/patients/id/"+id))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("testgetid")));


    }

    @Test
    public void updatePatient_shouldUpdatePatientInDb() throws Exception {
        Patient testPatient = new Patient("testupdate","it",LocalDate.parse("1992-05-21"), Patient.Gender.M);
        patientRepository.save(testPatient);
        int id = testPatient.getId();

        PatientRequestDTO requestDTO= new PatientRequestDTO(
                "testupdate",
                "it",
                LocalDate.of(1991,02,21),
                "M",
                "8 rue Martel",
                "111-222-333"

        );
        mockMvc.perform(put("/patients/update/id/"+id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk());






    }

}
