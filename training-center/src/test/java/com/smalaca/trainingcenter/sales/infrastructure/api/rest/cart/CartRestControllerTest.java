package com.smalaca.trainingcenter.sales.infrastructure.api.rest.cart;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smalaca.trainingcenter.sales.domain.cart.Cart;
import com.smalaca.trainingcenter.sales.domain.cart.CartAssertion;
import com.smalaca.trainingcenter.sales.domain.cart.CartTestFactory;
import com.smalaca.trainingcenter.sales.domain.training.TrainingId;
import com.smalaca.trainingcenter.sales.infrastructure.repository.jpa.cart.JpaCartTestRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(JpaCartTestRepository.class)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class CartRestControllerTest {
    @Autowired private MockMvc mockMvc;
    @Autowired private JpaCartTestRepository cartRepository;

    private final CartTestFactory cartFactory = CartTestFactory.cartTestFactory();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @AfterEach
    void tearDown() {
        cartRepository.deleteAll();
    }

    @Test
    void shouldFindAllCarts() throws Exception {
        UUID cartId1 = existingCart(List.of(id()));
        UUID cartId2 = existingCart(List.of(id(), id()));

        mockMvc.perform(get("/cart"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cartId").value(cartId1.toString()))
                .andExpect(jsonPath("$[0].trainingIds").isArray())
                .andExpect(jsonPath("$[1].cartId").value(cartId2.toString()))
                .andExpect(jsonPath("$[1].trainingIds").isArray());
    }

    @Test
    void shouldFindCartById() throws Exception {
        UUID trainingId = id();
        UUID cartId = existingCart(List.of(trainingId));

        mockMvc.perform(get("/cart/" + cartId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cartId").value(cartId.toString()))
                .andExpect(jsonPath("$.trainingIds[0]").value(trainingId.toString()));
    }

    @Test
    void shouldReturnNotFoundWhenCartDoesNotExist() throws Exception {
        UUID nonExistingCart = id();

        mockMvc.perform(get("/cart/" + nonExistingCart))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldAddTrainingToCart() throws Exception {
        UUID trainingIdOne = id();
        UUID trainingIdTwo = id();
        UUID cartId = existingCart(List.of(trainingIdOne));
        CartRequest request = new CartRequest(trainingIdTwo);

        mockMvc.perform(post("/cart/" + cartId + "/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        Optional<Cart> found = cartRepository.findById(cartId);
        assertThat(found).isPresent();
        CartAssertion.assertThat(found.get())
                .hasTrainings(2)
                .hasTraining(new TrainingId(trainingIdOne))
                .hasTraining(new TrainingId(trainingIdTwo));
    }

    @Test
    void shouldRemoveTrainingFromCart() throws Exception {
        UUID trainingIdOne = id();
        UUID trainingIdTwo = id();
        UUID cartId = existingCart(List.of(trainingIdOne, trainingIdTwo));
        CartRequest request = new CartRequest(trainingIdOne);

        mockMvc.perform(delete("/cart/" + cartId + "/remove")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        Optional<Cart> found = cartRepository.findById(cartId);
        assertThat(found).isPresent();
        CartAssertion.assertThat(found.get())
                .hasTrainings(1)
                .hasTraining(new TrainingId(trainingIdTwo));
    }

    private UUID existingCart(List<UUID> trainingIds) {
        UUID cartId = id();
        cartRepository.save(cartFactory.createCartView(cartId, trainingIds));

        return cartId;
    }

    private UUID id() {
        return UUID.randomUUID();
    }
}
