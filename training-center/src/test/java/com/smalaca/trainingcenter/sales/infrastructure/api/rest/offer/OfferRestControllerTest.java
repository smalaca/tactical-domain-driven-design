package com.smalaca.trainingcenter.sales.infrastructure.api.rest.offer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.smalaca.trainingcenter.sales.domain.cart.CartId;
import com.smalaca.trainingcenter.sales.domain.clock.Clock;
import com.smalaca.trainingcenter.sales.domain.offer.Money;
import com.smalaca.trainingcenter.sales.domain.offer.Offer;
import com.smalaca.trainingcenter.sales.domain.training.TrainingId;
import com.smalaca.trainingcenter.sales.infrastructure.api.rest.client.OfferTestDto;
import com.smalaca.trainingcenter.sales.infrastructure.api.rest.client.TrainingCenterClient;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(JpaOfferTestRepository.class)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class OfferRestControllerTest {
    @Autowired private MockMvc mockMvc;
    @Autowired private JpaOfferTestRepository offerRepository;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private final Clock clock = mock(Clock.class);
    private TrainingCenterClient trainingCenterClient;

    @BeforeEach
    void setUp() {
        trainingCenterClient = new TrainingCenterClient(mockMvc, objectMapper);
        given(clock.now()).willReturn(LocalDateTime.now());
    }

    @AfterEach
    void tearDown() {
        offerRepository.deleteAll();
    }

    @Test
    void shouldFindAllOffers() throws Exception {
        UUID cartId1 = id();
        UUID trainingId1 = id();
        int price1 = 100;
        UUID offerId1 = existingOffer(cartId1, List.of(new OfferItem(trainingId1, price1)));

        UUID cartId2 = id();
        UUID trainingId2a = id();
        int price2a = 200;
        UUID trainingId2b = id();
        int price2b = 300;
        UUID offerId2 = existingOffer(cartId2, List.of(new OfferItem(trainingId2a, price2a), new OfferItem(trainingId2b, price2b)));

        List<OfferTestDto> actual = trainingCenterClient.offers().findAll();

        assertThat(actual).hasSize(2)
                .anySatisfy(offer -> {
                    assertThat(offer.offerId()).isEqualTo(offerId1);
                    assertThat(offer.cartId()).isEqualTo(cartId1);
                    assertThat(offer.items()).hasSize(1)
                            .anySatisfy(item -> {
                                assertThat(item.trainingId()).isEqualTo(trainingId1);
                                assertThat(item.price()).isEqualByComparingTo(BigDecimal.valueOf(price1));
                            });
                })
                .anySatisfy(offer -> {
                    assertThat(offer.offerId()).isEqualTo(offerId2);
                    assertThat(offer.cartId()).isEqualTo(cartId2);
                    assertThat(offer.items()).hasSize(2)
                            .anySatisfy(item -> {
                                assertThat(item.trainingId()).isEqualTo(trainingId2a);
                                assertThat(item.price()).isEqualByComparingTo(BigDecimal.valueOf(price2a));
                            })
                            .anySatisfy(item -> {
                                assertThat(item.trainingId()).isEqualTo(trainingId2b);
                                assertThat(item.price()).isEqualByComparingTo(BigDecimal.valueOf(price2b));
                            });
                });
    }

    @Test
    void shouldFindOfferById() throws Exception {
        UUID cartId = id();
        UUID trainingId = id();
        int price = 123;
        UUID offerId = existingOffer(cartId, List.of(new OfferItem(trainingId, price)));

        OfferTestDto actual = trainingCenterClient.offers().findOne(offerId);

        assertThat(actual.offerId()).isEqualTo(offerId);
        assertThat(actual.cartId()).isEqualTo(cartId);
        assertThat(actual.items()).hasSize(1)
                .anySatisfy(item -> {
                    assertThat(item.trainingId()).isEqualTo(trainingId);
                    assertThat(item.price()).isEqualByComparingTo(BigDecimal.valueOf(price));
                });
    }

    @Test
    void shouldReturnNotFoundWhenOfferDoesNotExist() throws Exception {
        UUID nonExistingOffer = id();

        mockMvc.perform(get("/offer/" + nonExistingOffer))
                .andExpect(status().isNotFound());
    }

    private UUID existingOffer(UUID cartId, List<OfferItem> items) {
        Offer.Builder builder = new Offer.Builder(new CartId(cartId), clock);
        items.forEach(item -> builder.item(new TrainingId(item.trainingId), new Money(BigDecimal.valueOf(item.price))));
        Offer offer = builder.build();
        offerRepository.save(offer);

        return offer.getOfferId().value();
    }

    private record OfferItem(UUID trainingId, int price) {}

    private UUID id() {
        return UUID.randomUUID();
    }
}
