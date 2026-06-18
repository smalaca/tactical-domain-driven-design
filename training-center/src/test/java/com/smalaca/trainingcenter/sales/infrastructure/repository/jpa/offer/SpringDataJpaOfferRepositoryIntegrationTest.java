package com.smalaca.trainingcenter.sales.infrastructure.repository.jpa.offer;

import com.smalaca.trainingcenter.sales.domain.cart.CartId;
import com.smalaca.trainingcenter.sales.domain.clock.Clock;
import com.smalaca.trainingcenter.sales.domain.money.Money;
import com.smalaca.trainingcenter.sales.domain.offer.Offer;
import com.smalaca.trainingcenter.sales.domain.offer.OfferId;
import com.smalaca.trainingcenter.sales.domain.offer.OfferRepository;
import com.smalaca.trainingcenter.sales.domain.training.TrainingId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.smalaca.trainingcenter.sales.domain.offer.OfferAssertion.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@DataJpaTest
@Import(SpringDataJpaOfferRepository.class)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class SpringDataJpaOfferRepositoryIntegrationTest {
    @Autowired private OfferRepository offerRepository;
    @Autowired private JpaOfferRepository jpaOfferRepository;

    private final Clock clock = mock(Clock.class);

    @AfterEach
    void tearDown() {
        jpaOfferRepository.deleteAll();
    }

    @Test
    void shouldThrowExceptionWhenOfferNotFound() {
        OfferId offerId = OfferId.offerId();

        assertThatThrownBy(() -> offerRepository.findBy(offerId))
                .isInstanceOf(OfferNotFoundException.class);
    }

    @Test
    void shouldFindExistingOffer() {
        givenNow();
        CartId cartId = new CartId(UUID.randomUUID());
        Offer offer = new Offer.Builder(cartId, clock).build();
        offerRepository.save(offer);

        Offer actual = offerRepository.findBy(offer.getOfferId());

        assertThat(actual).hasCartId(cartId);
    }

    @Test
    void shouldFindSpecificOfferWhenMultipleExist() {
        givenNow();
        CartId cartId = new CartId(UUID.randomUUID());
        Offer offer = new Offer.Builder(cartId, clock).build();
        offerRepository.save(new Offer.Builder(new CartId(UUID.randomUUID()), clock).build());
        offerRepository.save(offer);
        offerRepository.save(new Offer.Builder(new CartId(UUID.randomUUID()), clock).build());

        Offer actual = offerRepository.findBy(offer.getOfferId());

        assertThat(actual).hasCartId(cartId);
    }

    @Test
    void shouldSaveAndLoadOfferWithItems() {
        LocalDateTime createdAt = givenNow();
        CartId cartId = new CartId(UUID.randomUUID());
        TrainingId trainingId1 = new TrainingId(UUID.randomUUID());
        TrainingId trainingId2 = new TrainingId(UUID.randomUUID());
        Money price1 = new Money(BigDecimal.valueOf(100));
        Money price2 = new Money(BigDecimal.valueOf(200));
        Offer offer = new Offer.Builder(cartId, clock)
                .item(trainingId1, price1)
                .item(trainingId2, price2)
                .build();
        offerRepository.save(offer);
        OfferId offerId = offer.getOfferId();

        Offer actual = offerRepository.findBy(offerId);

        assertThat(actual)
                .hasCartId(cartId)
                .hasItems(2)
                .hasItem(trainingId1, price1)
                .hasItem(trainingId2, price2)
                .hasCreatedAt(createdAt)
                .hasValidTo(createdAt.plusDays(10));
    }

    @NonNull
    private LocalDateTime givenNow() {
        LocalDateTime createdAt = LocalDateTime.now();
        given(clock.now()).willReturn(createdAt);
        return createdAt;
    }
}
