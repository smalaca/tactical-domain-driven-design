package com.smalaca.trainingcenter.sales.domain.offer;

import com.smalaca.annotations.architecture.DomainDrivenDesign;
import com.smalaca.trainingcenter.sales.domain.cart.CartId;
import com.smalaca.trainingcenter.sales.domain.clock.Clock;
import com.smalaca.trainingcenter.sales.domain.training.TrainingId;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@DomainDrivenDesign.AggregateRoot
@Entity
@Table(name = "OFFERS")
public class Offer {
    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "offer_id"))
    private OfferId offerId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "cart_id"))
    private CartId cartId;

    @ElementCollection
    @CollectionTable(name = "OFFER_ITEMS", joinColumns = @JoinColumn(name = "offer_id"))
    private Set<OfferItem> items;

    private LocalDateTime createdAt;
    private LocalDateTime validTo;

    private Offer() {}

    private Offer(Builder builder) {
        this.offerId = builder.offerId;
        this.cartId = builder.cartId;
        this.items = builder.items;
        this.createdAt = builder.createdAt;
        this.validTo = builder.validTo;
    }

    public OfferId getOfferId() {
        return offerId;
    }

    @DomainDrivenDesign.Factory
    public static class Builder {
        private final CartId cartId;
        private final Clock clock;
        private final OfferId offerId = OfferId.offerId();
        private final Set<OfferItem> items = new HashSet<>();
        private LocalDateTime createdAt;
        private LocalDateTime validTo;

        public Builder(CartId cartId, Clock clock) {
            this.cartId = cartId;
            this.clock = clock;
        }

        public Builder item(TrainingId trainingId, Money price) {
            items.add(new OfferItem(trainingId, price));
            return this;
        }

        public Offer build() {
            createdAt = clock.now();
            validTo = createdAt.plusDays(10);
            return new Offer(this);
        }
    }
}
