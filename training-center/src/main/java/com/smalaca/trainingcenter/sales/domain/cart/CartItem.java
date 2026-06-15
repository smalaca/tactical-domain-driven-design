package com.smalaca.trainingcenter.sales.domain.cart;

import com.smalaca.annotations.architecture.DomainDrivenDesign;
import com.smalaca.trainingcenter.sales.domain.training.TrainingId;

@DomainDrivenDesign.Entity
class CartItem {
    private final TrainingId trainingId;

    CartItem(TrainingId trainingId) {
        this.trainingId = trainingId;
    }

    boolean isFor(TrainingId trainingId) {
        return this.trainingId.equals(trainingId);
    }
}
