package com.smalaca.trainingcenter.sales.application.cart;

import com.smalaca.trainingcenter.sales.domain.cart.CartRepository;
import com.smalaca.trainingcenter.sales.domain.clock.Clock;
import com.smalaca.trainingcenter.sales.domain.offer.OfferRepository;
import com.smalaca.trainingcenter.sales.domain.offer.pricing.OfferPricingPolicy;
import com.smalaca.trainingcenter.sales.domain.offer.pricing.OfferPricingPolicyFactory;
import com.smalaca.trainingcenter.sales.domain.opentrainingservice.OpenTrainingService;
import com.smalaca.trainingcenter.sales.domain.promotion.PromotionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CartApplicationServiceFactory {

    @Bean
    CartApplicationService cartApplicationService(
            CartRepository cartRepository, OfferRepository offerRepository,
            Clock clock, OpenTrainingService openTrainingService,
            PromotionService promotionService) {
        OfferPricingPolicy offerPricingPolicy = offerPricingPolicy(promotionService);
        return new CartApplicationService(cartRepository, offerRepository, clock, openTrainingService, offerPricingPolicy);
    }

    private OfferPricingPolicy offerPricingPolicy(PromotionService promotionService) {
        return new OfferPricingPolicyFactory().create(promotionService);
    }
}
