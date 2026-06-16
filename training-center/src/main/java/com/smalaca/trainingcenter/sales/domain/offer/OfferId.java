package com.smalaca.trainingcenter.sales.domain.offer;

import com.smalaca.annotations.architecture.DomainDrivenDesign;
import jakarta.persistence.Embeddable;

import java.util.UUID;

@DomainDrivenDesign.ValueObject
@Embeddable
record OfferId(UUID value) {
    @DomainDrivenDesign.Factory
    static OfferId offerId() {
        return new OfferId(UUID.randomUUID());
    }
}
