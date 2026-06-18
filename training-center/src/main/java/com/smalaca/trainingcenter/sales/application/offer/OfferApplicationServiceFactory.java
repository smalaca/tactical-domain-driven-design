package com.smalaca.trainingcenter.sales.application.offer;

import com.smalaca.trainingcenter.sales.domain.clock.Clock;
import com.smalaca.trainingcenter.sales.domain.offer.*;
import com.smalaca.trainingcenter.sales.domain.offer.acceptance.OfferAcceptanceSpecification;
import com.smalaca.trainingcenter.sales.domain.offer.acceptance.OfferAcceptanceSpecificationFactory;
import com.smalaca.trainingcenter.sales.domain.opentrainingservice.OpenTrainingService;
import com.smalaca.trainingcenter.sales.domain.order.OrderRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OfferApplicationServiceFactory {

    @Bean
    OfferApplicationService offerApplicationService(
            OfferRepository offerRepository, OrderRepository orderRepository,
            Clock clock, OpenTrainingService openTrainingService) {
        OfferAcceptanceSpecification specification = offerAcceptanceSpecification(clock, openTrainingService);
        return new OfferApplicationService(offerRepository, orderRepository, specification, clock);
    }

    private OfferAcceptanceSpecification offerAcceptanceSpecification(Clock clock, OpenTrainingService openTrainingService) {
        return new OfferAcceptanceSpecificationFactory().create(clock, openTrainingService);
    }
}
