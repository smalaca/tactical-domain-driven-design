package com.smalaca.trainingcenter.sales.infrastructure.api.rest.cart;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smalaca.trainingcenter.sales.infrastructure.api.rest.client.CartTestDto;
import com.smalaca.trainingcenter.sales.infrastructure.api.rest.client.CartTestRequest;
import com.smalaca.trainingcenter.sales.infrastructure.api.rest.client.TrainingCenterClient;
import com.smalaca.trainingcenter.sales.domain.cart.Cart;
import com.smalaca.trainingcenter.sales.domain.cart.CartAssertion;
import com.smalaca.trainingcenter.sales.domain.cart.CartTestFactory;
import com.smalaca.trainingcenter.sales.domain.training.TrainingId;
import com.smalaca.trainingcenter.sales.infrastructure.repository.jpa.cart.JpaCartTestRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    private TrainingCenterClient trainingCenterClient;

    @BeforeEach
    void setUp() {
        trainingCenterClient = new TrainingCenterClient(mockMvc, objectMapper);
    }

    @AfterEach
    void tearDown() {
        cartRepository.deleteAll();
    }

    @Test
    void shouldFindAllCarts() throws Exception {
        UUID cartId1 = existingCart(List.of(id()));
        UUID cartId2 = existingCart(List.of(id(), id()));

        List<CartTestDto> actual = trainingCenterClient.carts().findAll();
        assertThat(actual).hasSize(2)
                .anySatisfy(cart -> {
                    assertThat(cart.cartId()).isEqualTo(cartId1);
                    assertThat(cart.trainingIds()).hasSize(1);
                })
                .anySatisfy(cart -> {
                    assertThat(cart.cartId()).isEqualTo(cartId2);
                    assertThat(cart.trainingIds()).hasSize(2);
                });
    }

    @Test
    void shouldFindCartById() throws Exception {
        UUID trainingId = id();
        UUID cartId = existingCart(List.of(trainingId));

        CartTestDto actual = trainingCenterClient.carts().findOne(cartId);
        assertThat(actual.cartId()).isEqualTo(cartId);
        assertThat(actual.trainingIds()).contains(trainingId);
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
        CartTestRequest request = new CartTestRequest(trainingIdTwo);

        trainingCenterClient.carts().addTraining(cartId, request);

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
        CartTestRequest request = new CartTestRequest(trainingIdOne);

        trainingCenterClient.carts().removeTraining(cartId, request);

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
