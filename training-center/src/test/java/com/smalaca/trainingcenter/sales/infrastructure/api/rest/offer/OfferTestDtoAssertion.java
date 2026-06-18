package com.smalaca.trainingcenter.sales.infrastructure.api.rest.offer;

import com.smalaca.trainingcenter.sales.infrastructure.api.rest.client.OfferTestDto;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

import java.math.BigDecimal;
import java.util.UUID;

class OfferTestDtoAssertion extends AbstractAssert<OfferTestDtoAssertion, OfferTestDto> {
    private OfferTestDtoAssertion(OfferTestDto actual) {
        super(actual, OfferTestDtoAssertion.class);
    }

    static OfferTestDtoAssertion assertThat(OfferTestDto actual) {
        return new OfferTestDtoAssertion(actual);
    }

    OfferTestDtoAssertion hasOfferId(UUID expected) {
        Assertions.assertThat(actual.offerId()).isEqualTo(expected);
        return this;
    }

    OfferTestDtoAssertion hasCartId(UUID expected) {
        Assertions.assertThat(actual.cartId()).isEqualTo(expected);
        return this;
    }

    OfferTestDtoAssertion hasItems(int expected) {
        Assertions.assertThat(actual.items()).hasSize(expected);
        return this;
    }

    OfferTestDtoAssertion hasItem(UUID expectedTrainingId, int expectedPrice) {
        Assertions.assertThat(actual.items())
                .anySatisfy(item -> {
                    Assertions.assertThat(item.trainingId()).isEqualTo(expectedTrainingId);
                    Assertions.assertThat(item.price()).isEqualByComparingTo(BigDecimal.valueOf(expectedPrice));
                });
        return this;
    }
}
