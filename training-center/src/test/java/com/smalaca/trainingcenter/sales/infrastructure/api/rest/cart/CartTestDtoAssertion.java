package com.smalaca.trainingcenter.sales.infrastructure.api.rest.cart;

import com.smalaca.trainingcenter.sales.infrastructure.api.rest.client.CartTestDto;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

import java.time.LocalDateTime;
import java.util.UUID;

class CartTestDtoAssertion extends AbstractAssert<CartTestDtoAssertion, CartTestDto> {
    private CartTestDtoAssertion(CartTestDto actual) {
        super(actual, CartTestDtoAssertion.class);
    }

    static CartTestDtoAssertion assertThat(CartTestDto actual) {
        return new CartTestDtoAssertion(actual);
    }

    CartTestDtoAssertion hasCartId(UUID expected) {
        Assertions.assertThat(actual.cartId()).isEqualTo(expected);
        return this;
    }

    CartTestDtoAssertion hasItems(int expected) {
        Assertions.assertThat(actual.items()).hasSize(expected);
        return this;
    }

    CartTestDtoAssertion hasItem(UUID expectedTrainingId) {
        Assertions.assertThat(actual.items())
                .anySatisfy(item -> Assertions.assertThat(item.trainingId()).isEqualTo(expectedTrainingId));
        return this;
    }

    CartTestDtoAssertion hasItemAddedAfter(UUID expectedTrainingId, LocalDateTime past) {
        Assertions.assertThat(actual.items())
                .anySatisfy(item -> {
                    Assertions.assertThat(item.trainingId()).isEqualTo(expectedTrainingId);
                    Assertions.assertThat(item.addedAt()).isAfter(past);
                });
        return this;
    }

    CartTestDtoAssertion allItemsAddedAfter(LocalDateTime past) {
        Assertions.assertThat(actual.items())
                .allSatisfy(item -> Assertions.assertThat(item.addedAt()).isAfter(past));
        return this;
    }
}
