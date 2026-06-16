package com.smalaca.trainingcenter.sales.domain.cart;

class OfferCanBeCreatedOnlyForActiveCartException extends RuntimeException {
    OfferCanBeCreatedOnlyForActiveCartException(CartId cartId) {
        super("Offer can be created only for active cart: " + cartId);
    }
}
