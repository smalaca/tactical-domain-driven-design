package com.smalaca.trainingcenter.sales.domain.offer.acceptance;

import com.smalaca.annotations.architecture.DomainDrivenDesign;

@DomainDrivenDesign.Specification
public interface OfferAcceptanceSpecification {
    void check(OfferAcceptanceParameters parameters);
}
