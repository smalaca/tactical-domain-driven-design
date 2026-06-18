package com.smalaca.trainingcenter.sales.domain.offer.acceptance;

import com.smalaca.trainingcenter.sales.domain.clock.Clock;
import com.smalaca.trainingcenter.sales.domain.opentrainingservice.OpenTrainingService;

import java.util.List;

public class OfferAcceptanceSpecificationFactory {
    public OfferAcceptanceSpecification create(Clock clock, OpenTrainingService openTrainingService) {
        return new CompositeOfferAcceptanceSpecification(List.of(
                new OfferIsNotExpiredSpecification(clock),
                new TrainingsStillAvailableSpecification(openTrainingService),
                new OfferStillValidSpecification()
        ));
    }
}
