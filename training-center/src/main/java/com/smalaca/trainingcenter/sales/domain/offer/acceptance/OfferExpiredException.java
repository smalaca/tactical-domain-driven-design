package com.smalaca.trainingcenter.sales.domain.offer.acceptance;

import com.smalaca.trainingcenter.sales.domain.offer.OfferId;

public class OfferExpiredException extends RuntimeException {
    OfferExpiredException(OfferId offerId) {
        super("Offer: " + offerId + " expired.");
    }
}
