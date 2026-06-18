package com.smalaca.trainingcenter.sales.domain.offer.acceptance;

import com.smalaca.annotations.architecture.DomainDrivenDesign;
import com.smalaca.trainingcenter.sales.domain.opentrainingservice.OpenTrainingService;
import com.smalaca.trainingcenter.sales.domain.training.TrainingId;

@DomainDrivenDesign.Specification
class TrainingsStillAvailableSpecification implements OfferAcceptanceSpecification {
    private final OpenTrainingService openTrainingService;

    TrainingsStillAvailableSpecification(OpenTrainingService openTrainingService) {
        this.openTrainingService = openTrainingService;
    }

    @Override
    public void check(OfferAcceptanceParameters parameters) {
        for (TrainingId trainingId : parameters.trainingIds()) {
            if (openTrainingService.hasAlreadyStarted(trainingId)) {
                throw new TrainingAlreadyStartedException(trainingId);
            }
        }
    }
}
