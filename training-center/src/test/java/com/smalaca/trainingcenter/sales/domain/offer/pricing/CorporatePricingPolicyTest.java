package com.smalaca.trainingcenter.sales.domain.offer.pricing;

import com.smalaca.trainingcenter.sales.domain.money.Money;
import com.smalaca.trainingcenter.sales.domain.promotion.PromotionCode;
import com.smalaca.trainingcenter.sales.domain.training.TrainingId;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CorporatePricingPolicyTest {
    private final CorporatePricingPolicy policy = new CorporatePricingPolicy(new BigDecimal("0.20"));

    @Test
    void shouldApplyCorporateDiscountWhenCustomerIsCorporate() {
        Money basePrice = new Money(BigDecimal.valueOf(100));
        OfferPricingParameters parameters = parameters(CustomerType.CORPORATE, basePrice);

        Money actual = policy.apply(parameters, basePrice);

        assertThat(actual.amount()).isEqualByComparingTo(BigDecimal.valueOf(80));
    }

    @Test
    void shouldLeavePriceUnchangedWhenCustomerIsIndividual() {
        Money basePrice = new Money(BigDecimal.valueOf(100));
        OfferPricingParameters parameters = parameters(CustomerType.INDIVIDUAL, basePrice);

        Money actual = policy.apply(parameters, basePrice);

        assertThat(actual.amount()).isEqualByComparingTo(BigDecimal.valueOf(100));
    }

    @Test
    void shouldLeavePriceUnchangedWhenCustomerTypeIsNull() {
        Money basePrice = new Money(BigDecimal.valueOf(100));
        OfferPricingParameters parameters = parameters(null, basePrice);

        Money actual = policy.apply(parameters, basePrice);

        assertThat(actual.amount()).isEqualByComparingTo(BigDecimal.valueOf(100));
    }

    private OfferPricingParameters parameters(CustomerType customerType, Money basePrice) {
        return new OfferPricingParameters(
                new TrainingId(UUID.randomUUID()),
                basePrice,
                LocalDateTime.of(2026, 7, 1, 10, 0),
                1,
                customerType,
                new PromotionCode(null)
        );
    }
}
