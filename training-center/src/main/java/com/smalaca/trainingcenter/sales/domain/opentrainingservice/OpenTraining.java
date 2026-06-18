package com.smalaca.trainingcenter.sales.domain.opentrainingservice;

import com.smalaca.trainingcenter.sales.domain.money.Money;
import com.smalaca.trainingcenter.sales.domain.training.TrainingId;

public record OpenTraining(TrainingId trainingId, TrainingStatus trainingStatus, Money price) {
    public boolean hasAlreadyStarted() {
        return trainingStatus != TrainingStatus.NOT_STARTED;
    }
}
