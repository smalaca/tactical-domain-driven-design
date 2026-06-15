package com.smalaca.trainingcenter.sales.domain.cart;

import com.smalaca.trainingcenter.sales.domain.training.TrainingId;

import java.util.List;
import java.util.UUID;

public class CartTestFactory {
    private CartTestFactory() {}

    public static CartTestFactory cartTestFactory() {
        return new CartTestFactory();
    }

    public Cart createCartView(UUID cartId, List<UUID> trainingIds) {
        Cart cart = new Cart(new CartId(cartId));
        trainingIds.forEach(id -> cart.add(new TrainingId(id)));

        return cart;
    }
}