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

    static CartException trainingHasAlreadyStarted(TrainingId trainingId) {
        return new CartException("Training: " + trainingId + " has already started.");
    }
}
