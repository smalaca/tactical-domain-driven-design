package com.smalaca.trainingcenter.sales.domain.cart;

import com.smalaca.trainingcenter.sales.domain.training.TrainingId;

class TrainingAlreadyStartedException extends RuntimeException {
    TrainingAlreadyStartedException(TrainingId trainingId) {
        super("Training: " + trainingId + " already started.");
    }
}
