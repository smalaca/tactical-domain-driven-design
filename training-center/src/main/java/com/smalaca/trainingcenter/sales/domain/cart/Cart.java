package com.smalaca.trainingcenter.sales.domain.cart;

import com.smalaca.annotations.architecture.DomainDrivenDesign;
import com.smalaca.trainingcenter.sales.domain.clock.Clock;
import com.smalaca.trainingcenter.sales.domain.opentrainingservice.OpenTrainingService;
import com.smalaca.trainingcenter.sales.domain.training.TrainingId;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

import java.util.HashSet;
import java.util.Set;

import static com.smalaca.trainingcenter.sales.domain.cart.CartStatus.ACTIVE;
import static com.smalaca.trainingcenter.sales.domain.cart.CartStatus.BLOCKED;

@DomainDrivenDesign.AggregateRoot
@Entity
@Table(name = "CARTS")
public class Cart {
    private static final int MAX_TRAININGS = 10;

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "cart_id"))
    private CartId cartId;

    @Enumerated(EnumType.STRING)
    private CartStatus status;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "CART_ITEMS", joinColumns = @JoinColumn(name = "cart_id"))
    private Set<CartItem> items = new HashSet<>();

    private Cart() {}

    public static Cart active(CartId cartId) {
        Cart cart = new Cart();
        cart.cartId = cartId;
        cart.status = ACTIVE;
        return cart;
    }

    public void add(TrainingId trainingId, Clock clock, OpenTrainingService openTrainingService) {
        if (isNotActive()) {
            throw CartException.isNotActive();
        }

        if (isFull()) {
            throw CartException.isFull();
        }

        if (openTrainingService.hasAlreadyStarted(trainingId)) {
            throw CartException.trainingHasAlreadyStarted(trainingId);
        }

        CartItem item = new CartItem(trainingId, clock.now());

        if (items.contains(item)) {
            throw CartException.trainingAlreadyInCart(trainingId);
        }

        items.add(item);
    }

    private boolean isNotActive() {
        return !ACTIVE.equals(status);
    }

    private boolean isFull() {
        return items.size() >= MAX_TRAININGS;
    }

    public void remove(TrainingId trainingId) {
        if (doesNotHave(trainingId)) {
            throw new TrainingNotFoundInCartException(trainingId);
        }

        items.removeIf(item -> item.isFor(trainingId));
    }

    private boolean doesNotHave(TrainingId trainingId) {
        return items.stream().noneMatch(item -> item.isFor(trainingId));
    }

    public void block() {
        status = BLOCKED;
    }
}
