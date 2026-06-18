package com.smalaca.trainingcenter.sales.domain.offer.acceptance;

import com.smalaca.trainingcenter.sales.domain.training.TrainingId;

public class TrainingAlreadyStartedException extends RuntimeException {
    TrainingAlreadyStartedException(TrainingId trainingId) {
        super("Training: " + trainingId + " already started.");
    }
}
