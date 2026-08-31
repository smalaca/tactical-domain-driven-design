package com.smalaca.trainingcenter.sales.domain.offer.pricing;

import com.smalaca.annotations.architecture.DomainDrivenDesign;
import com.smalaca.trainingcenter.sales.domain.money.Money;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@DomainDrivenDesign.Policy
public class EarlyBirdPricingPolicy implements OfferPricingPolicy {
    private final LocalDateTime cutoff;
    private final BigDecimal discountRate;

    public EarlyBirdPricingPolicy(LocalDateTime cutoff, BigDecimal discountRate) {
        this.cutoff = cutoff;
        this.discountRate = discountRate;
    }

    @Override
    public Money apply(OfferPricingParameters parameters, Money price) {
        if (parameters.requestedAt() != null && parameters.requestedAt().isBefore(cutoff)) {
            BigDecimal discount = price.amount().multiply(discountRate);
            return new Money(price.amount().subtract(discount));
        }
        return price;
    }
}
