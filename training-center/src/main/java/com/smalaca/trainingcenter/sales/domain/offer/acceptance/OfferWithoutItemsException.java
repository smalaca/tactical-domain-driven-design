package com.smalaca.trainingcenter.sales.domain.offer.acceptance;

import com.smalaca.trainingcenter.sales.domain.offer.OfferId;

public class OfferWithoutItemsException extends RuntimeException {
    OfferWithoutItemsException(OfferId offerId) {
        super("Offer: " + offerId + " without items.");
    }
}
