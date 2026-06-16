package com.smalaca.trainingcenter.sales.domain.cart;

import com.smalaca.trainingcenter.sales.domain.training.TrainingId;

class CannotChooseTrainingOutsideCartException extends RuntimeException {
    CannotChooseTrainingOutsideCartException(TrainingId trainingId) {
        super("Training: " + trainingId + " cannot be chosen outside the cart.");
    }
}
