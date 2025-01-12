package com.cleveloper.apidesign.controller;

import com.cleveloper.apidesign.model.Process;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProcessController.class)
@ComponentScan(basePackages = "com.cleveloper.apidesign.strategy")
class PurchaseOrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getProduct_Cash_ShouldReturnCashPayment() throws Exception {
        String code = "P01";

        mockMvc.perform(get("/api/v2/po")
                        .param("code", code))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(code))
                .andExpect(jsonPath("$.paymentMethod").value("CASH"));
    }

    @Test
    void getProduct_Card_ShouldReturnCardPayment() throws Exception {
        String code = "P02";

        mockMvc.perform(get("/api/v2/po")
                        .param("code", code))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(code))
                .andExpect(jsonPath("$.paymentMethod").value("CARD"));
    }

    @Test
    void createProduct_Cash_ShouldReturnCashPayment() throws Exception {
        Process process = new Process("P01");

        mockMvc.perform(post("/api/v2/po")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(process)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("P01"))
                .andExpect(jsonPath("$.paymentMethod").value("CASH"));
    }

    @Test
    void createProduct_Card_ShouldReturnCardPayment() throws Exception {
        Process process = new Process("P02");

        mockMvc.perform(post("/api/v2/po")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(process)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("P02"))
                .andExpect(jsonPath("$.paymentMethod").value("CARD"));
    }
} 