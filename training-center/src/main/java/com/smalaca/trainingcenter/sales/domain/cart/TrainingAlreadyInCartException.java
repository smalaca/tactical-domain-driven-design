package com.smalaca.trainingcenter.sales.domain.cart;

import com.smalaca.trainingcenter.sales.domain.training.TrainingId;

class TrainingAlreadyInCartException extends RuntimeException {
    TrainingAlreadyInCartException(TrainingId trainingId) {
        super("Training: " + trainingId + " is already in the cart.");
    }
}
