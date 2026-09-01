package com.smalaca.trainingcenter.sales.domain.offer.pricing;

import com.smalaca.trainingcenter.sales.domain.money.Money;
import com.smalaca.trainingcenter.sales.domain.promotion.PromotionCode;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CompositeOfferPricingPolicyTest {

    @Test
    void shouldApplyMultiplePricingPoliciesSequentially() {
        CompositeOfferPricingPolicy policy = new CompositeOfferPricingPolicy(List.of(
                new CorporatePricingPolicy(new BigDecimal("0.20")),
                new EarlyBirdPricingPolicy(LocalDateTime.of(2026, 6, 1, 0, 0), new BigDecimal("0.10"))
        ));

        OfferPricingParameters parameters = new OfferPricingParameters(
                LocalDateTime.of(2026, 5, 1, 10, 0),
                1,
                CustomerType.CORPORATE,
                new PromotionCode(null)
        );

        Money actual = policy.apply(parameters, new Money(BigDecimal.valueOf(100)));

        // 100 - 20% = 80; 80 - 10% = 72
        assertThat(actual.amount()).isEqualByComparingTo(BigDecimal.valueOf(72));
    }
}
