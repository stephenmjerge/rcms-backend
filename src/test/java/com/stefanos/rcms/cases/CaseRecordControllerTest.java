package com.stefanos.rcms.cases;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stefanos.rcms.audit.AuditLogRepository;
import com.stefanos.rcms.support.IntegrationTestBase;

@AutoConfigureMockMvc
class CaseRecordControllerTest extends IntegrationTestBase {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CaseRecordRepository caseRecordRepository;

    @Autowired
    private AuditLogRepository auditLogRepository;

    @BeforeEach
    void cleanDatabase() {
        auditLogRepository.deleteAll();
        caseRecordRepository.deleteAll();
    }

    @Test
    void createAndFetchCase() throws Exception {
        String externalReference = "CASE-TEST-" + UUID.randomUUID();
        String body = """
            {
              "externalReference": "%s",
              "title": "Test Case",
              "description": "Created by test",
              "status": "OPEN",
              "assignedTo": "alice"
            }
            """.formatted(externalReference);

        MvcResult created = mockMvc.perform(post("/cases")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .header("Authorization", "Bearer " + TestJwtHelper.caseWorkerToken()))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.externalReference").value(externalReference))
            .andExpect(jsonPath("$.assignedTo").value("alice"))
            .andReturn();

        JsonNode createdJson = objectMapper.readTree(created.getResponse().getContentAsString());
        long id = createdJson.get("id").asLong();

        String updateBody = """
            {
              "title": "Test Case",
              "description": "Updated by test",
              "status": "OPEN",
              "assignedTo": "bob"
            }
            """;

        mockMvc.perform(put("/cases/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateBody)
                .header("Authorization", "Bearer " + TestJwtHelper.caseWorkerToken()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.assignedTo").value("bob"));

        mockMvc.perform(get("/cases/{id}", id)
                .header("Authorization", "Bearer " + TestJwtHelper.caseWorkerToken()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.assignedTo").value("bob"));
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

    @Test
    void returnsStatusCounts() throws Exception {
        String openBody = """
            {
              "externalReference": "CASE-OPEN-%s",
              "title": "Open Case",
              "description": "Open",
              "status": "OPEN",
              "assignedTo": "alice"
            }
            """.formatted(UUID.randomUUID());

        String closedBody = """
            {
              "externalReference": "CASE-CLOSED-%s",
              "title": "Closed Case",
              "description": "Closed",
              "status": "CLOSED",
              "assignedTo": "bob"
            }
            """.formatted(UUID.randomUUID());

        mockMvc.perform(post("/cases")
                .contentType(MediaType.APPLICATION_JSON)
                .content(openBody)
                .header("Authorization", "Bearer " + TestJwtHelper.caseWorkerToken()))
            .andExpect(status().isCreated());

        mockMvc.perform(post("/cases")
                .contentType(MediaType.APPLICATION_JSON)
                .content(closedBody)
                .header("Authorization", "Bearer " + TestJwtHelper.caseWorkerToken()))
            .andExpect(status().isCreated());

        mockMvc.perform(get("/cases/stats/status")
                .header("Authorization", "Bearer " + TestJwtHelper.caseWorkerToken()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[?(@.status=='OPEN')].count").value(hasItem(1)))
            .andExpect(jsonPath("$[?(@.status=='CLOSED')].count").value(hasItem(1)))
            .andExpect(jsonPath("$[?(@.status=='IN_PROGRESS')].count").value(hasItem(0)));
    }
}
