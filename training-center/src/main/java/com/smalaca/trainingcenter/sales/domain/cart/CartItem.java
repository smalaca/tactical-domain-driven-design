package com.smalaca.trainingcenter.sales.domain.cart;

import com.smalaca.annotations.architecture.DomainDrivenDesign;
import com.smalaca.trainingcenter.sales.domain.training.TrainingId;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;

import java.time.LocalDateTime;
import java.util.Objects;

@DomainDrivenDesign.Entity
@Embeddable
class CartItem {
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "training_id"))
    private TrainingId trainingId;

    private LocalDateTime addedAt;

    private CartItem() {}

    CartItem(TrainingId trainingId, LocalDateTime addedAt) {
        this.trainingId = trainingId;
        this.addedAt = addedAt;
    }

    boolean isFor(TrainingId trainingId) {
        return this.trainingId.equals(trainingId);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CartItem cartItem = (CartItem) o;
        return Objects.equals(trainingId, cartItem.trainingId) && Objects.equals(addedAt, cartItem.addedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(trainingId, addedAt);
    }
}
