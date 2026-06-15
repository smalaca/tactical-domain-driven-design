package com.smalaca.trainingcenter.sales.domain.cart;

import com.smalaca.annotations.architecture.DomainDrivenDesign;
import com.smalaca.trainingcenter.sales.domain.training.TrainingId;

import java.util.HashSet;
import java.util.Set;

@DomainDrivenDesign.AggregateRoot
class Cart {
    private final CartId cartId;
    private final Set<CartItem> items = new HashSet<>();

    Cart(CartId cartId) {
        this.cartId = cartId;
    }

    void add(TrainingId trainingId) {
        CartItem item = new CartItem(trainingId);

        if (items.contains(item)) {
            throw new TrainingAlreadyInCartException(trainingId);
        }

        items.add(item);
    }

    void remove(TrainingId trainingId) {
        if (doesNotHave(trainingId)) {
            throw new TrainingNotFoundInCartException(trainingId);
        }

        items.removeIf(item -> item.isFor(trainingId));
    }

    private boolean doesNotHave(TrainingId trainingId) {
        return items.stream().noneMatch(item -> item.isFor(trainingId));
    }

    private boolean has(TrainingId trainingId) {
        return items.stream().anyMatch(item -> item.isFor(trainingId));
    }
}
