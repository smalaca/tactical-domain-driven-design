package com.smalaca.trainingcenter.sales.domain.cart;

import com.smalaca.trainingcenter.sales.domain.training.TrainingId;

class TrainingNotFoundInCartException extends RuntimeException {
    TrainingNotFoundInCartException(TrainingId trainingId) {
        super("Training: " + trainingId + " not found in the cart.");
    }
}
