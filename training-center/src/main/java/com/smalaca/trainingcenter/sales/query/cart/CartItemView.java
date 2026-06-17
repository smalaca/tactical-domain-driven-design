package com.smalaca.trainingcenter.sales.query.cart;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.time.LocalDateTime;
import java.util.UUID;

@Embeddable
public class CartItemView {
    @Column(name = "training_id")
    private UUID trainingId;

    @Column(name = "added_at")
    private LocalDateTime addedAt;

    private CartItemView() {}

    public UUID getTrainingId() {
        return trainingId;
    }

    public LocalDateTime getAddedAt() {
        return addedAt;
    }
}
