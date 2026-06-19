package com.smalaca.trainingcenter.sales.query.order;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "ORDERS")
public class OrderView {
    @Id
    @Column(name = "order_id")
    private UUID orderId;

    @Column(name = "offer_id")
    private UUID offerId;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "ORDER_ITEMS", joinColumns = @JoinColumn(name = "order_id"))
    private Set<OrderItemView> items;

    private LocalDateTime createdAt;

    protected OrderView() {}

    public UUID getOrderId() {
        return orderId;
    }

    public UUID getOfferId() {
        return offerId;
    }

    public Set<OrderItemView> getItems() {
        return items;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
