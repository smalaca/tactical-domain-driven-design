package com.smalaca.trainingcenter.sales.query.offer;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.math.BigDecimal;
import java.util.UUID;

@Embeddable
public class OfferItemView {
    @Column(name = "training_id")
    private UUID trainingId;

    @Column(name = "price")
    private BigDecimal price;

    private OfferItemView() {}

    public UUID getTrainingId() {
        return trainingId;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
