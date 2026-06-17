package com.smalaca.trainingcenter.sales.query.cart;

import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "CARTS")
public class CartView {
    @Id
    @Column(name = "cart_id")
    private UUID cartId;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "CART_ITEMS", joinColumns = @JoinColumn(name = "cart_id"))
    private List<CartItemView> items;

    private CartView() {}

    public UUID getCartId() {
        return cartId;
    }

    public List<CartItemView> getItems() {
        return items;
    }
}
