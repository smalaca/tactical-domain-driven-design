package com.smalaca.trainingcenter.sales.infrastructure.api.rest.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OfferClient {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    OfferClient(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    public List<OfferTestDto> findAll() throws Exception {
        String response = mockMvc.perform(get("/offer"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(response, new TypeReference<>() {});
    }

    public OfferTestDto findOne(UUID offerId) throws Exception {
        String response = mockMvc.perform(get("/offer/" + offerId))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(response, OfferTestDto.class);
    }

    public UUID accept(UUID offerId) throws Exception {
        String response = mockMvc.perform(post("/offer/" + offerId + "/accept"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        return UUID.fromString(response.replace("\"", ""));
    }
}
