package com.smalaca.trainingcenter.sales.infrastructure.api.rest.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CartClient {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    CartClient(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    public List<CartTestDto> findAll() throws Exception {
        String response = mockMvc.perform(get("/cart"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(response, new TypeReference<>() {});
    }

    public CartTestDto findOne(UUID cartId) throws Exception {
        String response = mockMvc.perform(get("/cart/" + cartId))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(response, CartTestDto.class);
    }

    public void addTraining(UUID cartId, SingleTrainingTestRequest request) throws Exception {
        mockMvc.perform(post("/cart/" + cartId + "/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    public void removeTraining(UUID cartId, SingleTrainingTestRequest request) throws Exception {
        mockMvc.perform(delete("/cart/" + cartId + "/remove")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    public void block(UUID cartId) throws Exception {
        mockMvc.perform(post("/cart/" + cartId + "/block"))
                .andExpect(status().isOk());
    }

    public UUID choose(UUID cartId, MultipleTrainingTestRequest request) throws Exception {
        String response = mockMvc.perform(post("/cart/" + cartId + "/choose")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(response, UUID.class);
    }
}
