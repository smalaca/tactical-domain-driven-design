package com.smalaca.trainingcenter.sales.infrastructure.api.rest.offer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.smalaca.trainingcenter.sales.domain.cart.CartId;
import com.smalaca.trainingcenter.sales.domain.clock.Clock;
import com.smalaca.trainingcenter.sales.domain.money.Money;
import com.smalaca.trainingcenter.sales.domain.offer.Offer;
import com.smalaca.trainingcenter.sales.domain.offer.OfferId;
import com.smalaca.trainingcenter.sales.domain.order.Order;
import com.smalaca.trainingcenter.sales.domain.order.OrderAssertion;
import com.smalaca.trainingcenter.sales.domain.order.OrderId;
import com.smalaca.trainingcenter.sales.domain.training.TrainingId;
import com.smalaca.trainingcenter.sales.infrastructure.api.rest.client.OfferTestDto;
import com.smalaca.trainingcenter.sales.infrastructure.api.rest.client.TrainingCenterClient;
import com.smalaca.trainingcenter.sales.infrastructure.repository.jpa.offer.JpaOfferTestRepository;
import com.smalaca.trainingcenter.sales.infrastructure.repository.jpa.order.JpaOrderTestRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.util.Pair;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.smalaca.trainingcenter.sales.infrastructure.api.rest.offer.OfferTestDtoAssertion.assertThat;
import static java.util.Arrays.stream;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import({JpaOfferTestRepository.class, JpaOrderTestRepository.class})
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class OfferRestControllerTest {
    @Autowired private MockMvc mockMvc;
    @Autowired private JpaOfferTestRepository offerRepository;
    @Autowired private JpaOrderTestRepository orderRepository;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private final Clock clock = mock(Clock.class);
    private TrainingCenterClient trainingCenterClient;

    @BeforeEach
    void setUp() {
        trainingCenterClient = new TrainingCenterClient(mockMvc, objectMapper);
    }

    @AfterEach
    void tearDown() {
        offerRepository.deleteAll();
        orderRepository.deleteAll();
    }

    @Test
    void shouldFindAllOffers() throws Exception {
        UUID trainingId1 = UUID.randomUUID();
        UUID trainingId2 = UUID.randomUUID();
        UUID trainingId3 = UUID.randomUUID();
        UUID cartId1 = UUID.randomUUID();
        UUID cartId2 = UUID.randomUUID();
        UUID offerId1 = existingOffer(cartId1, Pair.of(trainingId1, 10));
        UUID offerId2 = existingOffer(cartId2, Pair.of(trainingId2, 13), Pair.of(trainingId3, 42));

        List<OfferTestDto> actual = trainingCenterClient.offers().findAll();

        assertThat(actual).hasSize(2);
        assertThat(actual)
                .anySatisfy(offer -> assertThat(offer)
                        .hasCartId(cartId1)
                        .hasOfferId(offerId1)
                        .hasItems(1)
                        .hasItem(trainingId1, 10))
                .anySatisfy(offer -> assertThat(offer)
                        .hasCartId(cartId2)
                        .hasOfferId(offerId2)
                        .hasItems(2)
                        .hasItem(trainingId2, 13)
                        .hasItem(trainingId3, 42));
    }

    @Test
    void shouldFindOfferById() throws Exception {
        UUID cartId = UUID.randomUUID();
        UUID trainingId = UUID.randomUUID();
        UUID offerId = existingOffer(cartId, Pair.of(trainingId, 33));

        OfferTestDto actual = trainingCenterClient.offers().findOne(offerId);

        assertThat(actual)
                .hasOfferId(offerId)
                .hasCartId(cartId)
                .hasItems(1)
                .hasItem(trainingId, 33);
    }

    @Test
    void shouldReturnNotFoundWhenOfferDoesNotExist() throws Exception {
        UUID nonExistingOfferId = UUID.randomUUID();

        mockMvc.perform(get("/offer/" + nonExistingOfferId))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldAcceptOffer() throws Exception {
        UUID trainingId = UUID.randomUUID();
        UUID offerId = existingOffer(UUID.randomUUID(), Pair.of(trainingId, 78));

        UUID orderId = trainingCenterClient.offers().accept(offerId);

        Optional<Order> found = orderRepository.findById(orderId);
        assertThat(found).isPresent();
        OrderAssertion.assertThat(found.get())
                .hasOrderId(new OrderId(orderId))
                .hasOfferId(new OfferId(offerId))
                .hasItems(1)
                .hasItem(new TrainingId(trainingId), new Money(BigDecimal.valueOf(78)));
    }

    private UUID existingOffer(UUID cartId, Pair<UUID, Integer>... trainings) {
        given(clock.now()).willReturn(LocalDateTime.now());
        Offer.Builder builder = new Offer.Builder(new CartId(cartId), clock);
        stream(trainings).forEach(training -> {
            builder.item(new TrainingId(training.getFirst()), new Money(BigDecimal.valueOf(training.getSecond())));
        });

        Offer offer = builder.build();
        offerRepository.save(offer);

        return offer.getOfferId().value();
    }
}
