package com.smalaca.trainingcenter.sales.domain.offer;

import com.smalaca.annotations.architecture.DomainDrivenDesign;
import com.smalaca.trainingcenter.sales.domain.cart.CartId;
import com.smalaca.trainingcenter.sales.domain.clock.Clock;
import com.smalaca.trainingcenter.sales.domain.money.Money;
import com.smalaca.trainingcenter.sales.domain.offer.acceptance.OfferAcceptanceParameters;
import com.smalaca.trainingcenter.sales.domain.offer.acceptance.OfferAcceptanceSpecification;
import com.smalaca.trainingcenter.sales.domain.order.Order;
import com.smalaca.trainingcenter.sales.domain.training.TrainingId;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

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

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "OFFER_ITEMS", joinColumns = @JoinColumn(name = "offer_id"))
    private Set<OfferItem> items;

    private LocalDateTime createdAt;
    private LocalDateTime validTo;

    @Enumerated(EnumType.STRING)
    private OfferStatus status;

    protected Offer() {}

    private Offer(Builder builder) {
        this.offerId = builder.offerId;
        this.cartId = builder.cartId;
        this.items = builder.items;
        this.createdAt = builder.createdAt;
        this.validTo = builder.validTo;
        this.status = builder.status;
    }

    public OfferId getOfferId() {
        return offerId;
    }

    @DomainDrivenDesign.Factory
    public Order accept(OfferAcceptanceSpecification specification, Clock clock) {
        specification.check(offerAcceptanceParameters());
        this.status = OfferStatus.ACCEPTED;

        Order.Builder builder = new Order.Builder(offerId, clock);
        items.forEach(item -> item.accept(builder));

        return builder.build();
    }

    private OfferAcceptanceParameters offerAcceptanceParameters() {
        return new OfferAcceptanceParameters(offerId, validTo, trainingIds());
    }

    private Set<TrainingId> trainingIds() {
        return items.stream().map(OfferItem::trainingId).collect(toSet());
    }

    @DomainDrivenDesign.Factory
    public static class Builder {
        private final CartId cartId;
        private final Clock clock;
        private final OfferId offerId = OfferId.offerId();
        private final Set<OfferItem> items = new HashSet<>();
        private LocalDateTime createdAt;
        private LocalDateTime validTo;
        private OfferStatus status;

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
            status = OfferStatus.CREATED;
            return new Offer(this);
        }
    }
}
