package com.smalaca.trainingcenter.sales.query;

import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "CARTS")
class CartView {
    @Id
    @Column(name = "cart_id")
    private UUID cartId;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "CART_ITEMS", joinColumns = @JoinColumn(name = "cart_id"))
    @Column(name = "training_id")
    private List<UUID> trainingIds;

    private CartView() {
    }

    UUID getCartId() {
        return cartId;
    }

    List<UUID> getTrainingIds() {
        return trainingIds;
    }
}
