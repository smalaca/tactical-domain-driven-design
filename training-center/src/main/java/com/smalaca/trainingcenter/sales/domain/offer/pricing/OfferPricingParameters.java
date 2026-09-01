package com.smalaca.trainingcenter.sales.domain.offer.pricing;

import com.smalaca.trainingcenter.sales.domain.promotion.PromotionCode;

import java.time.LocalDateTime;

public record OfferPricingParameters(
        LocalDateTime requestedAt,
        int participants,
        CustomerType customerType,
        PromotionCode promotionCode) {

    public OfferPricingParameters(LocalDateTime requestedAt) {
        this(requestedAt, 1, CustomerType.INDIVIDUAL, new PromotionCode(null));
    }
}
