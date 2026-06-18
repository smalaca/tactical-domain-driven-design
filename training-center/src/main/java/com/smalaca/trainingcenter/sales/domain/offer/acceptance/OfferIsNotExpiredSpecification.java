package com.smalaca.trainingcenter.sales.domain.offer.acceptance;

import com.smalaca.annotations.architecture.DomainDrivenDesign;
import com.smalaca.trainingcenter.sales.domain.clock.Clock;

@DomainDrivenDesign.Specification
class OfferIsNotExpiredSpecification implements OfferAcceptanceSpecification {
    private final Clock clock;

    OfferIsNotExpiredSpecification(Clock clock) {
        this.clock = clock;
    }

    @Override
    public void check(OfferAcceptanceParameters parameters) {
        if (parameters.validTo().isBefore(clock.now())) {
            throw new OfferExpiredException(parameters.offerId());
        }
    }
}
