package com.smalaca.trainingcenter.sales.infrastructure.api.rest.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OrderClient {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    OrderClient(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    public List<OrderTestDto> findAll() throws Exception {
        String response = mockMvc.perform(get("/order"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(response, new TypeReference<>() {});
    }

    public OrderTestDto findOne(UUID orderId) throws Exception {
        String response = mockMvc.perform(get("/order/" + orderId))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(response, OrderTestDto.class);
    }
}
