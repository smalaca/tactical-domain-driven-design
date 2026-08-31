package com.smalaca.trainingcenter.sales.domain.offer.pricing;

import com.smalaca.annotations.architecture.DomainDrivenDesign;
import com.smalaca.trainingcenter.sales.domain.money.Money;

import java.math.BigDecimal;

@DomainDrivenDesign.Policy
public class CorporatePricingPolicy implements OfferPricingPolicy {
    private final BigDecimal discountRate;

    public CorporatePricingPolicy(BigDecimal discountRate) {
        this.discountRate = discountRate;
    }

    @Override
    public Money apply(OfferPricingParameters parameters, Money price) {
        if (parameters.customerType() != null && parameters.customerType().isCorporate()) {
            BigDecimal discount = price.amount().multiply(discountRate);
            return new Money(price.amount().subtract(discount));
        }
        return price;
    }
}
