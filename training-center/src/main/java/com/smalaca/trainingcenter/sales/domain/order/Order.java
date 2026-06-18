package com.smalaca.trainingcenter.sales.domain.order;

import com.smalaca.annotations.architecture.DomainDrivenDesign;
import com.smalaca.trainingcenter.sales.domain.clock.Clock;
import com.smalaca.trainingcenter.sales.domain.money.Money;
import com.smalaca.trainingcenter.sales.domain.offer.OfferId;
import com.smalaca.trainingcenter.sales.domain.training.TrainingId;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@DomainDrivenDesign.AggregateRoot
@Entity
@Table(name = "ORDERS")
public class Order {
    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "order_id"))
    private OrderId orderId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "offer_id"))
    private OfferId offerId;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "ORDER_ITEMS", joinColumns = @JoinColumn(name = "order_id"))
    private Set<OrderItem> items;

    private LocalDateTime createdAt;

    private Order() {}

    private Order(Builder builder) {
        this.orderId = builder.orderId;
        this.offerId = builder.offerId;
        this.items = builder.items;
        this.createdAt = builder.createdAt;
    }

    public OrderId getOrderId() {
        return orderId;
    }

    @DomainDrivenDesign.Factory
    public static class Builder {
        private final OfferId offerId;
        private final Clock clock;
        private final OrderId orderId = OrderId.orderId();
        private final Set<OrderItem> items = new HashSet<>();
        private LocalDateTime createdAt;

        public Builder(OfferId offerId, Clock clock) {
            this.offerId = offerId;
            this.clock = clock;
        }

        public Builder item(TrainingId trainingId, Money price) {
            items.add(new OrderItem(trainingId, price));
            return this;
        }

        public Order build() {
            createdAt = clock.now();
            return new Order(this);
        }
    }
}
