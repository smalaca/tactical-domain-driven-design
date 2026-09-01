package com.smalaca.trainingcenter.sales.domain.offer.pricing;

import com.smalaca.annotations.architecture.DomainDrivenDesign;
import com.smalaca.trainingcenter.sales.domain.money.Money;

import java.util.List;

@DomainDrivenDesign.Policy
class CompositeOfferPricingPolicy implements OfferPricingPolicy {
    private final List<OfferPricingPolicy> policies;

    CompositeOfferPricingPolicy(List<OfferPricingPolicy> policies) {
        this.policies = policies;
    }

    @Override
    public Money apply(OfferPricingParameters parameters, Money price) {
        Money finalPrice = price;
        for (OfferPricingPolicy policy : policies) {
            finalPrice = policy.apply(parameters, finalPrice);
        }
        return finalPrice;
    }
}
