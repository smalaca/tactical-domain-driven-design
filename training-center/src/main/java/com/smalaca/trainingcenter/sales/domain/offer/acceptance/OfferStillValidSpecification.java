package com.smalaca.trainingcenter.sales.domain.offer.acceptance;

import com.smalaca.annotations.architecture.DomainDrivenDesign;

@DomainDrivenDesign.Specification
class OfferStillValidSpecification implements OfferAcceptanceSpecification {
    @Override
    public void check(OfferAcceptanceParameters parameters) {
        if (parameters.trainingIds().isEmpty()) {
            throw new OfferWithoutItemsException(parameters.offerId());
        }
    }
}
