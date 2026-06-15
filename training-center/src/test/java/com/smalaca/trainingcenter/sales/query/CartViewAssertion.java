package com.smalaca.trainingcenter.sales.query;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

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
        Assertions.assertThat(actual.getTrainingIds()).hasSize(expected);
        return this;
    }

    CartViewAssertion hasTraining(UUID expected) {
        Assertions.assertThat(actual.getTrainingIds()).contains(expected);
        return this;
    }
}
