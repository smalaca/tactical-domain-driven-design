package com.smalaca.trainingcenter.sales.domain.offer.pricing;

import com.smalaca.trainingcenter.sales.domain.money.Money;
import com.smalaca.trainingcenter.sales.domain.promotion.PromotionCode;
import com.smalaca.trainingcenter.sales.domain.training.TrainingId;

import java.time.LocalDateTime;

public record OfferPricingParameters(
        TrainingId trainingId,
        Money basePrice,
        LocalDateTime requestedAt,
        int participants,
        CustomerType customerType,
        PromotionCode promotionCode) {
}
