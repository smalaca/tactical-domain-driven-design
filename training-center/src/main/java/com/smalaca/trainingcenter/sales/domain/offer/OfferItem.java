package com.smalaca.trainingcenter.sales.domain.offer;

import com.smalaca.annotations.architecture.DomainDrivenDesign;
import com.smalaca.trainingcenter.sales.domain.training.TrainingId;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;

@DomainDrivenDesign.Entity
@Embeddable
class OfferItem {
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "training_id"))
    private TrainingId trainingId;

    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "price"))
    private Money price;

    private OfferItem() {}

    OfferItem(TrainingId trainingId, Money price) {
        this.trainingId = trainingId;
        this.price = price;
    }
}
