package com.smalaca.trainingcenter.sales.domain.cart;

import com.smalaca.trainingcenter.sales.domain.training.TrainingId;

public class CartException extends RuntimeException {
    private CartException(String message) {
        super(message);
    }

    static CartException trainingAlreadyInCart(CartId cartId, TrainingId trainingId) {
        return new CartException("Training: " + trainingId + " is already in the cart: " + cartId);
    }

    static CartException isNotActive(CartId cartId) {
        return new CartException("Cart: " + cartId + " is not active.");
    }

    static CartException isFull(CartId cartId) {
        return new CartException("Cart: " + cartId + " is full.");
    }

    static CartException trainingNotFoundInCart(CartId cartId, TrainingId trainingId) {
        return new CartException("Training: " + trainingId + " not found in the cart: " + cartId);
    }

    static CartException cannotCreateOfferFromEmptyCart(CartId cartId) {
        return new CartException("Cannot create offer from empty cart: " + cartId);
    }

    static CartException offerCanBeCreatedOnlyForActiveCart(CartId cartId) {
        return new CartException("Offer can be created only for active cart: " + cartId);
    }

    static CartException cannotChooseTrainingOutsideCart(CartId cartId, TrainingId trainingId) {
        return new CartException("Training: " + trainingId + " cannot be chosen outside the cart: " + cartId);
    }

    static CartException trainingNotFound(CartId cartId, TrainingId trainingId) {
        return new CartException("Training: " + trainingId + " not found in cart: " + cartId);
    }

    static CartException trainingAlreadyStarted(CartId cartId, TrainingId trainingId) {
        return new CartException("Training: " + trainingId + " already started in cart: " + cartId);
    }

    static CartException isAlreadyBlocked(CartId cartId) {
        return new CartException("Cart: " + cartId + " is already blocked.");
    }

    static CartException isAlreadyActive(CartId cartId) {
        return new CartException("Cart: " + cartId + " is already active.");
    }
}
