package com.stefanos.rcms.cases;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class CaseRecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createAndFetchCase() throws Exception {
        String externalReference = "CASE-TEST-" + UUID.randomUUID();
        String body = """
            {
              "externalReference": "%s",
              "title": "Test Case",
              "description": "Created by test",
              "status": "OPEN"
            }
            """.formatted(externalReference);

        mockMvc.perform(post("/cases")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .header("Authorization", "Bearer " + TestJwtHelper.caseWorkerToken()))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.externalReference").value(externalReference));

        mockMvc.perform(get("/cases")
                .header("Authorization", "Bearer " + TestJwtHelper.caseWorkerToken()))
            .andExpect(status().isOk());
    }

    @Test
    void rejectsMissingTitle() throws Exception {
        String body = """
            {
              "externalReference": "CASE-TEST-2",
              "description": "Missing title",
              "status": "OPEN"
            }
            """;

        mockMvc.perform(post("/cases")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .header("Authorization", "Bearer " + TestJwtHelper.caseWorkerToken()))
            .andExpect(status().isBadRequest());
    }
}
