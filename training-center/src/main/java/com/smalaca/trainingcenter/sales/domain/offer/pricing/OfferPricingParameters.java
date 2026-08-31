package com.smalaca.trainingcenter.sales.domain.offer.pricing;

import com.smalaca.annotations.architecture.DomainDrivenDesign;
import com.smalaca.trainingcenter.sales.domain.money.Money;
import com.smalaca.trainingcenter.sales.domain.training.TrainingId;

import java.time.LocalDateTime;

@DomainDrivenDesign.ValueObject
public record OfferPricingParameters(
        TrainingId trainingId,
        Money basePrice,
        LocalDateTime requestedAt,
        int participants,
        CustomerType customerType,
        PromotionCode promotionCode) {

    public OfferPricingParameters(TrainingId trainingId, Money basePrice, LocalDateTime requestedAt) {
        this(trainingId, basePrice, requestedAt, 1, CustomerType.INDIVIDUAL, PromotionCode.none());
    }
}
