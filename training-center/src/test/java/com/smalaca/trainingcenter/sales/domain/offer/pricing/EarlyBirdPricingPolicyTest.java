package com.smalaca.trainingcenter.sales.domain.offer.pricing;

import com.smalaca.trainingcenter.sales.domain.money.Money;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class EarlyBirdPricingPolicyTest {
    private final EarlyBirdPricingPolicy policy = new EarlyBirdPricingPolicy(
            LocalDateTime.of(2026, 6, 1, 0, 0),
            new BigDecimal("0.10")
    );

    @Test
    void shouldApplyEarlyBirdDiscountWhenRequestedBeforeCutoff() {
        Money basePrice = new Money(BigDecimal.valueOf(100));
        OfferPricingParameters parameters = parameters(LocalDateTime.of(2026, 5, 1, 10, 0));

        Money actual = policy.apply(parameters, basePrice);

        assertThat(actual.amount()).isEqualByComparingTo(BigDecimal.valueOf(90));
    }

    @Test
    void shouldLeavePriceUnchangedWhenRequestedOnOrAfterCutoff() {
        Money basePrice = new Money(BigDecimal.valueOf(100));
        OfferPricingParameters parameters = parameters(LocalDateTime.of(2026, 6, 1, 0, 0));

        Money actual = policy.apply(parameters, basePrice);

        assertThat(actual.amount()).isEqualByComparingTo(BigDecimal.valueOf(100));
    }

    @Test
    void shouldLeavePriceUnchangedWhenRequestedAtIsNull() {
        Money basePrice = new Money(BigDecimal.valueOf(100));
        OfferPricingParameters parameters = parameters(null);

        Money actual = policy.apply(parameters, basePrice);

        assertThat(actual.amount()).isEqualByComparingTo(BigDecimal.valueOf(100));
    }

    private OfferPricingParameters parameters(LocalDateTime requestedAt) {
        return new OfferPricingParameters(requestedAt);
    }
}
