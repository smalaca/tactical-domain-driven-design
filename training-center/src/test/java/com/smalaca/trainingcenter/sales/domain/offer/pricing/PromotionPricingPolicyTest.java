package com.smalaca.trainingcenter.sales.domain.offer.pricing;

import com.smalaca.trainingcenter.sales.domain.money.Money;
import com.smalaca.trainingcenter.sales.domain.promotion.PromotionCode;
import com.smalaca.trainingcenter.sales.domain.promotion.PromotionService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

class PromotionPricingPolicyTest {
    private final PromotionService promotionService = Mockito.mock(PromotionService.class);
    private final PromotionPricingPolicy policy = new PromotionPricingPolicy(new BigDecimal("0.10"), promotionService);

    @Test
    void shouldApplyPromotionDiscountWhenPromotionCodeIsValidAndAvailable() {
        PromotionCode promotionCode = new PromotionCode("PROMO10");
        given(promotionService.isAvailable(promotionCode)).willReturn(true);
        Money basePrice = new Money(BigDecimal.valueOf(100));
        OfferPricingParameters parameters = parameters(promotionCode);

        Money actual = policy.apply(parameters, basePrice);

        assertThat(actual.amount()).isEqualByComparingTo(BigDecimal.valueOf(90));
    }

    @Test
    void shouldLeavePriceUnchangedWhenPromotionCodeIsValidButNotAvailable() {
        PromotionCode promotionCode = new PromotionCode("PROMO10");
        given(promotionService.isAvailable(promotionCode)).willReturn(false);
        Money basePrice = new Money(BigDecimal.valueOf(100));
        OfferPricingParameters parameters = parameters(promotionCode);

        Money actual = policy.apply(parameters, basePrice);

        assertThat(actual.amount()).isEqualByComparingTo(BigDecimal.valueOf(100));
    }

    @Test
    void shouldLeavePriceUnchangedWhenPromotionCodeIsNone() {
        Money basePrice = new Money(BigDecimal.valueOf(100));
        OfferPricingParameters parameters = parameters(new PromotionCode(null));

        Money actual = policy.apply(parameters, basePrice);

        assertThat(actual.amount()).isEqualByComparingTo(BigDecimal.valueOf(100));
    }

    @Test
    void shouldLeavePriceUnchangedWhenPromotionCodeIsEmpty() {
        Money basePrice = new Money(BigDecimal.valueOf(100));
        OfferPricingParameters parameters = parameters(new PromotionCode("   "));

        Money actual = policy.apply(parameters, basePrice);

        assertThat(actual.amount()).isEqualByComparingTo(BigDecimal.valueOf(100));
    }

    @Test
    void shouldLeavePriceUnchangedWhenPromotionCodeIsNull() {
        Money basePrice = new Money(BigDecimal.valueOf(100));
        OfferPricingParameters parameters = parameters(null);

        Money actual = policy.apply(parameters, basePrice);

        assertThat(actual.amount()).isEqualByComparingTo(BigDecimal.valueOf(100));
    }

    private OfferPricingParameters parameters(PromotionCode promotionCode) {
        return new OfferPricingParameters(
                LocalDateTime.of(2026, 7, 1, 10, 0),
                1,
                CustomerType.INDIVIDUAL,
                promotionCode
        );
    }
}
