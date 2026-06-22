package com.smalaca.trainingcenter.sales.infrastructure.api.rest.cart;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.smalaca.trainingcenter.sales.domain.cart.Cart;
import com.smalaca.trainingcenter.sales.domain.cart.CartAssertion;
import com.smalaca.trainingcenter.sales.domain.cart.CartId;
import com.smalaca.trainingcenter.sales.domain.cart.CartTestFactory;
import com.smalaca.trainingcenter.sales.domain.offer.Offer;
import com.smalaca.trainingcenter.sales.domain.offer.OfferAssertion;
import com.smalaca.trainingcenter.sales.domain.training.TrainingId;
import com.smalaca.trainingcenter.sales.infrastructure.api.rest.client.CartTestDto;
import com.smalaca.trainingcenter.sales.infrastructure.api.rest.client.MultipleTrainingTestRequest;
import com.smalaca.trainingcenter.sales.infrastructure.api.rest.client.SingleTrainingTestRequest;
import com.smalaca.trainingcenter.sales.infrastructure.api.rest.client.TrainingCenterClient;
import com.smalaca.trainingcenter.sales.infrastructure.repository.jpa.cart.JpaCartTestRepository;
import com.smalaca.trainingcenter.sales.infrastructure.repository.jpa.offer.JpaOfferTestRepository;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static com.smalaca.trainingcenter.sales.infrastructure.api.rest.cart.CartTestDtoAssertion.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import({JpaCartTestRepository.class, JpaOfferTestRepository.class})
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class CartRestControllerTest {
    @Autowired private MockMvc mockMvc;
    @Autowired private JpaCartTestRepository cartRepository;
    @Autowired private JpaOfferTestRepository offerRepository;

    private final CartTestFactory cartFactory = CartTestFactory.cartTestFactory();
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private TrainingCenterClient trainingCenterClient;

    @BeforeEach
    void setUp() {
        trainingCenterClient = new TrainingCenterClient(mockMvc, objectMapper);
    }

    @AfterEach
    void tearDown() {
        cartRepository.deleteAll();
        offerRepository.deleteAll();
    }

    @Test
    void shouldFindAllCarts() throws Exception {
        LocalDateTime past = LocalDateTime.now().minusMinutes(1);
        UUID trainingIdOne = id();
        UUID trainingIdTwo = id();
        UUID trainingIdThree = id();
        UUID cartId1 = existingCart(List.of(trainingIdOne));
        UUID cartId2 = existingCart(List.of(trainingIdTwo, trainingIdThree));

        List<CartTestDto> actual = trainingCenterClient.carts().findAll();
        assertThat(actual).hasSize(2);
        assertThat(actual)
                .anySatisfy(cart -> assertThat(cart)
                        .hasCartId(cartId1)
                        .hasItems(1)
                        .hasItemAddedAfter(trainingIdOne, past))
                .anySatisfy(cart -> assertThat(cart)
                        .hasCartId(cartId2)
                        .hasItems(2)
                        .allItemsAddedAfter(past)
                        .hasItem(trainingIdTwo)
                        .hasItem(trainingIdThree));
    }

    @Test
    void shouldFindCartById() throws Exception {
        LocalDateTime past = LocalDateTime.now().minusMinutes(1);
        UUID trainingId = id();
        UUID cartId = existingCart(List.of(trainingId));

        CartTestDto actual = trainingCenterClient.carts().findOne(cartId);
        assertThat(actual)
                .hasCartId(cartId)
                .hasItems(1)
                .hasItemAddedAfter(trainingId, past);
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
        SingleTrainingTestRequest request = new SingleTrainingTestRequest(trainingIdTwo);

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
        SingleTrainingTestRequest request = new SingleTrainingTestRequest(trainingIdOne);

        trainingCenterClient.carts().removeTraining(cartId, request);

        Optional<Cart> found = cartRepository.findById(cartId);
        assertThat(found).isPresent();
        CartAssertion.assertThat(found.get())
                .hasTrainings(1)
                .hasTraining(new TrainingId(trainingIdTwo));
    }

    @Test
    void shouldBlockCart() throws Exception {
        UUID cartId = existingCart();

        trainingCenterClient.carts().block(cartId);

        Optional<Cart> found = cartRepository.findById(cartId);
        assertThat(found).isPresent();
        CartAssertion.assertThat(found.get()).isBlocked();
    }

    @Test
    void shouldUnblockCart() throws Exception {
        UUID cartId = existingCart();
        trainingCenterClient.carts().block(cartId);

        trainingCenterClient.carts().unblock(cartId);

        Optional<Cart> found = cartRepository.findById(cartId);
        assertThat(found).isPresent();
        CartAssertion.assertThat(found.get()).isActive();
    }

    @Test
    void shouldEmptyCart() throws Exception {
        UUID trainingIdOne = id();
        UUID trainingIdTwo = id();
        UUID cartId = existingCart(List.of(trainingIdOne, trainingIdTwo));

        trainingCenterClient.carts().empty(cartId);

        Optional<Cart> found = cartRepository.findById(cartId);
        assertThat(found).isPresent();
        CartAssertion.assertThat(found.get()).hasTrainings(0);
    }

    @Test
    void shouldChooseTrainingsFromCart() throws Exception {
        UUID trainingId1 = id();
        UUID trainingId2 = id();
        UUID trainingId3 = id();
        UUID cartId = existingCart(List.of(trainingId1, trainingId2, trainingId3));
        MultipleTrainingTestRequest request = new MultipleTrainingTestRequest(List.of(trainingId1, trainingId2));

        UUID offerId = trainingCenterClient.carts().choose(cartId, request);

        Optional<Offer> found = offerRepository.findById(offerId);
        assertThat(found).isPresent();
        OfferAssertion.assertThat(found.get())
                .hasCartId(new CartId(cartId))
                .hasItems(2);
    }

    private UUID existingCart() {
        return existingCart(List.of(id()));
    }

    private UUID existingCart(List<UUID> trainingIds) {
        UUID cartId = id();
        cartRepository.save(cartFactory.createCart(cartId, trainingIds));

        return cartId;
    }

    private UUID id() {
        return UUID.randomUUID();
    }
}
