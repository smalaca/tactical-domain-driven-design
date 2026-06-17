package com.smalaca.trainingcenter.sales.infrastructure.api.rest.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.MockMvc;

public class TrainingCenterClient {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    public TrainingCenterClient(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    public CartClient carts() {
        return new CartClient(mockMvc, objectMapper);
    }

    public OfferClient offers() {
        return new OfferClient(mockMvc, objectMapper);
    }
}
