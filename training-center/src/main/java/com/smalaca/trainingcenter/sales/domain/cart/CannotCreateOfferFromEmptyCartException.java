package com.smalaca.trainingcenter.sales.domain.cart;

class CannotCreateOfferFromEmptyCartException extends RuntimeException {
    CannotCreateOfferFromEmptyCartException(CartId cartId) {
        super("Cannot create offer from empty cart: " + cartId);
    }
}
