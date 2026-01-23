package com.stefanos.rcms.cases;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
        String body = """
            {
              "externalReference": "CASE-TEST-1",
              "title": "Test Case",
              "description": "Created by test",
              "status": "OPEN"
            }
            """;

        mockMvc.perform(post("/cases")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .header("Authorization", "Bearer " + TestJwtHelper.caseWorkerToken()))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.externalReference").value("CASE-TEST-1"));

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
