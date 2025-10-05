package com.tsbsaas.simulator.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class RunControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void startRunWithInlineScenarioAndFetchRun() throws Exception {
        String body = "{" +
                "\"scenario\":{\"name\":\"TestScenario\",\"components\":[{" +
                "\"id\":\"db1\",\"type\":\"RDBMS\",\"params\":{\"vCPU\":2,\"memMB\":1024,\"storageGB\":10}," +
                "\"behavior\":{\"opProfiles\":{\"select\":\"Normal(2,0.5)\"},\"concurrency\":8,\"queueCapacity\":100}" +
                "}]," +
                "\"links\":[]," +
                "\"workload\":{\"type\":\"poisson\",\"rps\":50,\"durationSec\":2,\"mix\":[{\"op\":\"select\",\"pct\":100}],\"payloadKB\":1,\"hotKeyPct\":0}" +
                "}}";

        MvcResult result = mockMvc.perform(post("/runs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.runId").exists())
                .andReturn();

        String json = result.getResponse().getContentAsString();
        String runId = json.replaceAll(".*\"runId\":\"([^\"]+)\".*", "$1");
        assertThat(runId).isNotBlank();

        mockMvc.perform(get("/runs/" + runId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(runId));
    }

    @Test
    void createScenarioThenRetrieve() throws Exception {
        String scenario = "{" +
                "\"name\":\"DirectScenario\",\"components\":[{" +
                "\"id\":\"app1\",\"type\":\"AppServer\",\"params\":{\"vCPU\":1,\"memMB\":512}," +
                "\"behavior\":{\"opProfiles\":{\"call\":\"Normal(1,0.2)\"},\"concurrency\":16,\"queueCapacity\":100}" +
                "}]," +
                "\"links\":[]," +
                "\"workload\":{\"type\":\"poisson\",\"rps\":10,\"durationSec\":1,\"mix\":[{\"op\":\"call\",\"pct\":100}],\"payloadKB\":1,\"hotKeyPct\":0}" +
                "}";

        MvcResult create = mockMvc.perform(post("/scenarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(scenario))
                .andExpect(status().isOk())
                .andReturn();
        String id = create.getResponse().getContentAsString().replace("\"", "").trim();
        assertThat(id).isNotBlank();

        mockMvc.perform(get("/scenarios/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));
    }
}

