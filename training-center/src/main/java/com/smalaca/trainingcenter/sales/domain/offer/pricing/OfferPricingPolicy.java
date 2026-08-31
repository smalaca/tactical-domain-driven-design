package com.smalaca.trainingcenter.sales.domain.offer.pricing;

import com.smalaca.annotations.architecture.DomainDrivenDesign;
import com.smalaca.trainingcenter.sales.domain.money.Money;

@DomainDrivenDesign.Policy
public interface OfferPricingPolicy {
    Money apply(OfferPricingParameters parameters, Money price);
}
