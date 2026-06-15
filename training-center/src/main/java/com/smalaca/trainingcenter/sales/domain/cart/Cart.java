package com.smalaca.trainingcenter.sales.domain.cart;

import com.smalaca.annotations.architecture.DomainDrivenDesign;
import com.smalaca.trainingcenter.sales.domain.training.TrainingId;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

import java.util.HashSet;
import java.util.Set;

@DomainDrivenDesign.AggregateRoot
@Entity
@Table(name = "CARTS")
public class Cart {
    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "cart_id"))
    private CartId cartId;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "CART_ITEMS", joinColumns = @JoinColumn(name = "cart_id"))
    private Set<CartItem> items = new HashSet<>();

    private Cart() {
    }

    public Cart(CartId cartId) {
        this.cartId = cartId;
    }

    public void add(TrainingId trainingId) {
        CartItem item = new CartItem(trainingId);

        if (items.contains(item)) {
            throw new TrainingAlreadyInCartException(trainingId);
        }

        items.add(item);
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
}
