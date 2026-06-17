package com.smalaca.trainingcenter.sales.query.cart;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

import java.time.LocalDateTime;
import java.util.UUID;

class CartViewAssertion extends AbstractAssert<CartViewAssertion, CartView> {
    private CartViewAssertion(CartView actual) {
        super(actual, CartViewAssertion.class);
    }

    static CartViewAssertion assertThat(CartView actual) {
        return new CartViewAssertion(actual);
    }

    CartViewAssertion hasId(UUID expected) {
        Assertions.assertThat(actual.getCartId()).isEqualTo(expected);
        return this;
    }

    CartViewAssertion hasTrainings(int expected) {
        Assertions.assertThat(actual.getItems()).hasSize(expected);
        return this;
    }

    CartViewAssertion hasTraining(UUID expectedTrainingId, LocalDateTime expectedAddedAt) {
        Assertions.assertThat(actual.getItems())
                .anySatisfy(item -> {
                    Assertions.assertThat(item.getTrainingId()).isEqualTo(expectedTrainingId);
                    Assertions.assertThat(item.getAddedAt()).isEqualToIgnoringNanos(expectedAddedAt);
                });
        return this;
    }
}
