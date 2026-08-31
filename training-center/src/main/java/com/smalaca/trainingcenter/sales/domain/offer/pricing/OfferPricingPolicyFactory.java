package com.smalaca.trainingcenter.sales.domain.offer.pricing;

import com.smalaca.annotations.architecture.DomainDrivenDesign;

@DomainDrivenDesign.Factory
public class OfferPricingPolicyFactory {
    public OfferPricingPolicy create() {
        return (parameters, price) -> price;
    }
}
