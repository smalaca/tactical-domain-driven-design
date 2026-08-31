package com.smalaca.trainingcenter.sales.domain.offer.pricing;

import com.smalaca.annotations.architecture.DomainDrivenDesign;

@DomainDrivenDesign.ValueObject
public enum CustomerType {
    INDIVIDUAL,
    CORPORATE;

    public boolean isCorporate() {
        return this == CORPORATE;
    }
}
