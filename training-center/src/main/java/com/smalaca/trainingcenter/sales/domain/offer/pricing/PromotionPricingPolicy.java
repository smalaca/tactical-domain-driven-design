package com.smalaca.trainingcenter.sales.domain.offer.pricing;

import com.smalaca.annotations.architecture.DomainDrivenDesign;
import com.smalaca.trainingcenter.sales.domain.money.Money;

import java.math.BigDecimal;

@DomainDrivenDesign.Policy
public class PromotionPricingPolicy implements OfferPricingPolicy {
    private final BigDecimal discountRate;

    public PromotionPricingPolicy(BigDecimal discountRate) {
        this.discountRate = discountRate;
    }

    @Override
    public Money apply(OfferPricingParameters parameters, Money price) {
        if (parameters.promotionCode() != null && parameters.promotionCode().isValid()) {
            BigDecimal discount = price.amount().multiply(discountRate);
            return new Money(price.amount().subtract(discount));
        }
        return price;
    }
}
