package com.smalaca.trainingcenter.sales.domain.offer.acceptance;

import com.smalaca.annotations.architecture.DomainDrivenDesign;

import java.util.List;

@DomainDrivenDesign.Specification
class CompositeOfferAcceptanceSpecification implements OfferAcceptanceSpecification {
    private final List<OfferAcceptanceSpecification> specifications;

    CompositeOfferAcceptanceSpecification(List<OfferAcceptanceSpecification> specifications) {
        this.specifications = specifications;
    }

    @Override
    public void check(OfferAcceptanceParameters parameters) {
        specifications.forEach(specification -> specification.check(parameters));
    }
}
