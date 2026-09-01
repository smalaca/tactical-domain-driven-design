package com.smalaca.trainingcenter.sales.domain.offer.pricing;

import com.smalaca.annotations.architecture.DomainDrivenDesign;
import com.smalaca.trainingcenter.sales.domain.money.Money;

import java.math.BigDecimal;

@DomainDrivenDesign.Policy
class GroupPricingPolicy implements OfferPricingPolicy {
    private final int minParticipants;
    private final BigDecimal discountRate;

    GroupPricingPolicy(int minParticipants, BigDecimal discountRate) {
        this.minParticipants = minParticipants;
        this.discountRate = discountRate;
    }

    @Override
    public Money apply(OfferPricingParameters parameters, Money price) {
        if (parameters.participants() >= minParticipants) {
            BigDecimal discount = price.amount().multiply(discountRate);
            return new Money(price.amount().subtract(discount));
        }
        return price;
    }
}
