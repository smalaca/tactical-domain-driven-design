package com.smalaca.trainingcenter.sales.domain.offer.pricing;

import com.smalaca.annotations.architecture.DomainDrivenDesign;
import com.smalaca.trainingcenter.sales.domain.promotion.PromotionService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@DomainDrivenDesign.Factory
public class OfferPricingPolicyFactory {
    public OfferPricingPolicy create(PromotionService promotionService) {
        return new CompositeOfferPricingPolicy(List.of(
                new CorporatePricingPolicy(new BigDecimal("0.20")),
                new EarlyBirdPricingPolicy(LocalDateTime.of(2026, 6, 1, 0, 0), new BigDecimal("0.10")),
                new GroupPricingPolicy(5, new BigDecimal("0.15")),
                new PromotionPricingPolicy(new BigDecimal("0.10"), promotionService)
        ));
    }
}
