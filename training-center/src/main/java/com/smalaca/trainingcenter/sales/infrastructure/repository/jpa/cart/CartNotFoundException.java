package com.smalaca.trainingcenter.sales.infrastructure.repository.jpa.cart;

import com.smalaca.trainingcenter.sales.domain.cart.CartId;

class CartNotFoundException extends RuntimeException {
    CartNotFoundException(CartId cartId) {
        super("Cart with id: " + cartId.value() + " not found.");
    }
}
