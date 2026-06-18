package com.smalaca.trainingcenter.sales.domain.offer;

import com.smalaca.annotations.architecture.DomainDrivenDesign;

@DomainDrivenDesign.ValueObject
enum OfferStatus {
    CREATED,
    ACCEPTED
}
