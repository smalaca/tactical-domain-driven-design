package com.smalaca.trainingcenter.sales.domain.offer.pricing;

import com.smalaca.trainingcenter.sales.domain.money.Money;
import com.smalaca.trainingcenter.sales.domain.promotion.PromotionCode;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class GroupPricingPolicyTest {
    private final GroupPricingPolicy policy = new GroupPricingPolicy(5, new BigDecimal("0.15"));

    @Test
    void shouldApplyGroupDiscountWhenParticipantsReachThreshold() {
        Money basePrice = new Money(BigDecimal.valueOf(100));
        OfferPricingParameters parameters = parameters(5);

        Money actual = policy.apply(parameters, basePrice);

        assertThat(actual.amount()).isEqualByComparingTo(BigDecimal.valueOf(85));
    }

    @Test
    void shouldLeavePriceUnchangedWhenParticipantsLessThanThreshold() {
        Money basePrice = new Money(BigDecimal.valueOf(100));
        OfferPricingParameters parameters = parameters(4);

        Money actual = policy.apply(parameters, basePrice);

        assertThat(actual.amount()).isEqualByComparingTo(BigDecimal.valueOf(100));
    }

    private OfferPricingParameters parameters(int participants) {
        return new OfferPricingParameters(
                LocalDateTime.of(2026, 7, 1, 10, 0),
                participants,
                CustomerType.INDIVIDUAL,
                new PromotionCode(null)
        );
    }
}
