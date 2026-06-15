package com.smalaca.trainingcenter.sales.infrastructure.repository.jpa.cart;

import com.smalaca.trainingcenter.sales.domain.cart.Cart;
import com.smalaca.trainingcenter.sales.domain.cart.CartId;

import java.util.Optional;
import java.util.UUID;

public class JpaCartTestRepository {
    private final JpaCartRepository repository;

    JpaCartTestRepository(JpaCartRepository repository) {
        this.repository = repository;
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public void save(Cart cart) {
        repository.save(cart);
    }

    public Optional<Cart> findById(UUID cartId) {
        return repository.findById(new CartId(cartId));
    }
}