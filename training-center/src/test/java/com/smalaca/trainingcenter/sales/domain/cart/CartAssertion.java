package com.smalaca.trainingcenter.sales.domain.cart;

import com.smalaca.trainingcenter.sales.domain.training.TrainingId;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

import java.time.LocalDateTime;
import java.util.Set;

import static com.smalaca.trainingcenter.sales.domain.cart.CartStatus.ACTIVE;
import static com.smalaca.trainingcenter.sales.domain.cart.CartStatus.BLOCKED;

public class CartAssertion extends AbstractAssert<CartAssertion, Cart> {
    private CartAssertion(Cart actual) {
        super(actual, CartAssertion.class);
    }

    public static CartAssertion assertThat(Cart actual) {
        return new CartAssertion(actual);
    }

    public CartAssertion hasId(CartId expected) {
        Assertions.assertThat(actual).extracting("cartId").isEqualTo(expected);
        return this;
    }

    public CartAssertion isActive() {
        return hasStatus(ACTIVE);
    }

    public CartAssertion isBlocked() {
        return hasStatus(BLOCKED);
    }

    private CartAssertion hasStatus(CartStatus expected) {
        Assertions.assertThat(actual).extracting("status").isEqualTo(expected);
        return this;
    }

    public CartAssertion hasTrainings(int expected) {
        Assertions.assertThat(actual).extracting("items").satisfies(items -> {
            Set<?> itemsSet = (Set<?>) items;
            Assertions.assertThat(itemsSet).hasSize(expected);
        });
        return this;
    }

    public CartAssertion hasTraining(TrainingId expected) {
        Assertions.assertThat(actual).extracting("items").satisfies(items -> {
            Set<?> itemsSet = (Set<?>) items;

            Assertions.assertThat(itemsSet).anySatisfy(item -> {
                Assertions.assertThat(item).extracting("trainingId").isEqualTo(expected);
                Assertions.assertThat(item).extracting("addedAt").isNotNull();
            });
        });

        return this;
    }

    public CartAssertion hasTraining(TrainingId expectedTrainingId, LocalDateTime expectedAddedAt) {
        Assertions.assertThat(actual).extracting("items").satisfies(items -> {
            Set<?> itemsSet = (Set<?>) items;

            Assertions.assertThat(itemsSet).anySatisfy(item -> {
                Assertions.assertThat(item).extracting("trainingId").isEqualTo(expectedTrainingId);
                Assertions.assertThat(item).extracting("addedAt").isEqualTo(expectedAddedAt);
            });
        });

        return this;
    }
}
