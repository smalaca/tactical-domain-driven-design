package com.smalaca.trainingcenter.sales.domain.offer.pricing;

import com.smalaca.annotations.architecture.DomainDrivenDesign;
import com.smalaca.trainingcenter.sales.domain.money.Money;
import com.smalaca.trainingcenter.sales.domain.promotion.PromotionCode;
import com.smalaca.trainingcenter.sales.domain.promotion.PromotionService;

import java.math.BigDecimal;

@DomainDrivenDesign.Policy
class PromotionPricingPolicy implements OfferPricingPolicy {
    private final BigDecimal discountRate;
    private final PromotionService promotionService;

    public PromotionPricingPolicy(BigDecimal discountRate, PromotionService promotionService) {
        this.discountRate = discountRate;
        this.promotionService = promotionService;
    }

    @Override
    public Money apply(OfferPricingParameters parameters, Money price) {
        PromotionCode promotionCode = parameters.promotionCode();

        if (promotionCode != null && promotionCode.isValid() && promotionService.isAvailable(promotionCode)) {
            BigDecimal discount = price.amount().multiply(discountRate);
            return new Money(price.amount().subtract(discount));
        }
        return price;
    }
}
