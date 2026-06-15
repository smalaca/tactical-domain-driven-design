package com.smalaca.trainingcenter.sales.domain.cart;

import com.smalaca.annotations.architecture.DomainDrivenDesign;
import com.smalaca.trainingcenter.sales.domain.training.TrainingId;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;

@DomainDrivenDesign.Entity
@Embeddable
class CartItem {
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "training_id"))
    private TrainingId trainingId;

    private CartItem() {
    }

    CartItem(TrainingId trainingId) {
        this.trainingId = trainingId;
    }

    boolean isFor(TrainingId trainingId) {
        return this.trainingId.equals(trainingId);
    }
}
