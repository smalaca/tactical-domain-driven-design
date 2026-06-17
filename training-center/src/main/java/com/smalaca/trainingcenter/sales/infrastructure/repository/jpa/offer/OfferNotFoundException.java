package com.smalaca.trainingcenter.sales.infrastructure.repository.jpa.offer;

import com.smalaca.trainingcenter.sales.domain.offer.OfferId;

class OfferNotFoundException extends RuntimeException {
    OfferNotFoundException(OfferId offerId) {
        super("Offer with id: " + offerId.value() + " not found.");
    }
}
