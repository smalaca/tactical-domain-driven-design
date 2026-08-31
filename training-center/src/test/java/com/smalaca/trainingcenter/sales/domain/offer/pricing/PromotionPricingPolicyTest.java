package com.smalaca.trainingcenter.sales.domain.offer.pricing;

import com.smalaca.trainingcenter.sales.domain.money.Money;
import com.smalaca.trainingcenter.sales.domain.training.TrainingId;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class PromotionPricingPolicyTest {
    private final PromotionPricingPolicy policy = new PromotionPricingPolicy(new BigDecimal("0.10"));

    @Test
    void shouldApplyPromotionDiscountWhenPromotionCodeIsValid() {
        Money basePrice = new Money(BigDecimal.valueOf(100));
        OfferPricingParameters parameters = parameters(PromotionCode.of("PROMO10"), basePrice);

        Money actual = policy.apply(parameters, basePrice);

        assertThat(actual.amount()).isEqualByComparingTo(BigDecimal.valueOf(90));
    }

    @Test
    void shouldLeavePriceUnchangedWhenPromotionCodeIsNone() {
        Money basePrice = new Money(BigDecimal.valueOf(100));
        OfferPricingParameters parameters = parameters(PromotionCode.none(), basePrice);

        Money actual = policy.apply(parameters, basePrice);

        assertThat(actual.amount()).isEqualByComparingTo(BigDecimal.valueOf(100));
    }

    @Test
    void shouldLeavePriceUnchangedWhenPromotionCodeIsEmpty() {
        Money basePrice = new Money(BigDecimal.valueOf(100));
        OfferPricingParameters parameters = parameters(PromotionCode.of("   "), basePrice);

        Money actual = policy.apply(parameters, basePrice);

        assertThat(actual.amount()).isEqualByComparingTo(BigDecimal.valueOf(100));
    }

    @Test
    void shouldLeavePriceUnchangedWhenPromotionCodeIsNull() {
        Money basePrice = new Money(BigDecimal.valueOf(100));
        OfferPricingParameters parameters = parameters(null, basePrice);

        Money actual = policy.apply(parameters, basePrice);

        assertThat(actual.amount()).isEqualByComparingTo(BigDecimal.valueOf(100));
    }

    private OfferPricingParameters parameters(PromotionCode promotionCode, Money basePrice) {
        return new OfferPricingParameters(
                new TrainingId(UUID.randomUUID()),
                basePrice,
                LocalDateTime.of(2026, 7, 1, 10, 0),
                1,
                CustomerType.INDIVIDUAL,
                promotionCode
        );
    }
}
