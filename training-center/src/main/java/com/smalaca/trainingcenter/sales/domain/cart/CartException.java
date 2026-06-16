package com.smalaca.trainingcenter.sales.domain.cart;

import com.smalaca.trainingcenter.sales.domain.training.TrainingId;

public class CartException extends RuntimeException {
    private CartException(String message) {
        super(message);
    }

    static CartException trainingAlreadyInCart(TrainingId trainingId) {
        return new CartException("Training: " + trainingId + " is already in the cart.");
    }

    static CartException isNotActive() {
        return new CartException("Cart is not active.");
    }

    static CartException isFull() {
        return new CartException("Cart is full.");
    }

    static CartException trainingNotFoundInCart(TrainingId trainingId) {
        return new CartException("Training: " + trainingId + " not found in the cart.");
    }

    static CartException cannotCreateOfferFromEmptyCart(CartId cartId) {
        return new CartException("Cannot create offer from empty cart: " + cartId);
    }

    static CartException offerCanBeCreatedOnlyForActiveCart(CartId cartId) {
        return new CartException("Offer can be created only for active cart: " + cartId);
    }

    static CartException cannotChooseTrainingOutsideCart(TrainingId trainingId) {
        return new CartException("Training: " + trainingId + " cannot be chosen outside the cart.");
    }

    static CartException trainingNotFound(TrainingId trainingId) {
        return new CartException("Training: " + trainingId + " not found.");
    }

    static CartException trainingAlreadyStarted(TrainingId trainingId) {
        return new CartException("Training: " + trainingId + " already started.");
    }
}
