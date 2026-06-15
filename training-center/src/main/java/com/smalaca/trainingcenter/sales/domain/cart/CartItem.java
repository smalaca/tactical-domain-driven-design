package com.smalaca.trainingcenter.sales.domain.cart;

import com.smalaca.annotations.architecture.DomainDrivenDesign;
import com.smalaca.trainingcenter.sales.domain.training.TrainingId;
import jakarta.persistence.*;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CartItem cartItem = (CartItem) o;
        return Objects.equals(trainingId, cartItem.trainingId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(trainingId);
    }
}
