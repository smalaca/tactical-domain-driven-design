package com.smalaca.trainingcenter.sales.query.offer;

import com.smalaca.trainingcenter.sales.domain.cart.CartId;
import com.smalaca.trainingcenter.sales.domain.clock.Clock;
import com.smalaca.trainingcenter.sales.domain.offer.Money;
import com.smalaca.trainingcenter.sales.domain.offer.Offer;
import com.smalaca.trainingcenter.sales.domain.offer.OfferRepository;
import com.smalaca.trainingcenter.sales.domain.training.TrainingId;
import com.smalaca.trainingcenter.sales.infrastructure.repository.jpa.offer.SpringDataJpaOfferRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.smalaca.trainingcenter.sales.query.offer.OfferViewAssertion.assertThat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@DataJpaTest
@Import({OfferQueryService.class, SpringDataJpaOfferRepository.class})
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class OfferQueryServiceIntegrationTest {
    @Autowired private OfferQueryService queryService;
    @Autowired private JpaOfferViewRepository viewRepository;
    @Autowired private OfferRepository offerRepository;

    private final Clock clock = mock(Clock.class);

    @AfterEach
    void tearDown() {
        viewRepository.deleteAll();
    }

    @Test
    void shouldFindNothingWhenNoOffers() {
        UUID offerId = id();

        Optional<OfferView> actual = queryService.findOneById(offerId);

        assertThat(actual).isEmpty();
    }

    @Test
    void shouldFindOfferById() {
        UUID cartId = id();
        UUID trainingId1 = id();
        BigDecimal price1 = BigDecimal.valueOf(100);
        UUID trainingId2 = id();
        BigDecimal price2 = BigDecimal.valueOf(200);
        LocalDateTime createdAt = LocalDateTime.now().minusMinutes(10);
        UUID offerId = givenOffer(cartId, createdAt, trainingId1, price1, trainingId2, price2);

        Optional<OfferView> actual = queryService.findOneById(offerId);

        assertThat(actual).isPresent();
        assertThat(actual.get())
                .hasId(offerId)
                .hasCartId(cartId)
                .hasCreationDateTime(createdAt)
                .hasValidTo(createdAt.plusDays(10))
                .hasItems(2)
                .hasItem(trainingId1, price1)
                .hasItem(trainingId2, price2);
    }

    @Test
    void shouldFindAllOffers() {
        UUID cartId1 = id();
        LocalDateTime createdAt1 = LocalDateTime.now().minusMinutes(20);
        UUID trainingId1 = id();
        BigDecimal price1 = BigDecimal.valueOf(150);
        UUID offerId1 = givenOffer(cartId1, createdAt1, trainingId1, price1);

        UUID cartId2 = id();
        LocalDateTime createdAt2 = LocalDateTime.now().minusMinutes(10);
        UUID trainingId2 = id();
        BigDecimal price2 = BigDecimal.valueOf(250);
        UUID offerId2 = givenOffer(cartId2, createdAt2, trainingId2, price2);

        List<OfferView> actual = queryService.findAll();

        assertThat(actual).hasSize(2);
        assertThat(actual)
                .anySatisfy(view -> assertThat(view)
                        .hasId(offerId1)
                        .hasCartId(cartId1)
                        .hasCreationDateTime(createdAt1)
                        .hasItems(1)
                        .hasItem(trainingId1, price1))
                .anySatisfy(view -> assertThat(view)
                        .hasId(offerId2)
                        .hasCartId(cartId2)
                        .hasCreationDateTime(createdAt2)
                        .hasItems(1)
                        .hasItem(trainingId2, price2));
    }

    private UUID id() {
        return UUID.randomUUID();
    }

    private UUID givenOffer(UUID cartId, LocalDateTime createdAt, Object... trainingIdsAndPrices) {
        given(clock.now()).willReturn(createdAt);
        Offer.Builder builder = new Offer.Builder(new CartId(cartId), clock);

        for (int i = 0; i < trainingIdsAndPrices.length; i += 2) {
            UUID trainingId = (UUID) trainingIdsAndPrices[i];
            BigDecimal price = (BigDecimal) trainingIdsAndPrices[i + 1];
            builder.item(new TrainingId(trainingId), new Money(price));
        }

        Offer offer = builder.build();
        offerRepository.save(offer);

        return offer.getOfferId().value();
    }
}
