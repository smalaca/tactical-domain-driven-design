package com.smalaca.trainingcenter.sales.query.offer;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

class OfferViewAssertion extends AbstractAssert<OfferViewAssertion, OfferView> {
    private OfferViewAssertion(OfferView actual) {
        super(actual, OfferViewAssertion.class);
    }

    static OfferViewAssertion assertThat(OfferView actual) {
        return new OfferViewAssertion(actual);
    }

    OfferViewAssertion hasId(UUID expected) {
        Assertions.assertThat(actual.getOfferId()).isEqualTo(expected);
        return this;
    }

    OfferViewAssertion hasCartId(UUID expected) {
        Assertions.assertThat(actual.getCartId()).isEqualTo(expected);
        return this;
    }

    OfferViewAssertion hasCreationDateTime(LocalDateTime expected) {
        Assertions.assertThat(actual.getCreatedAt()).isEqualToIgnoringNanos(expected);
        return this;
    }

    OfferViewAssertion hasValidTo(LocalDateTime expected) {
        Assertions.assertThat(actual.getValidTo()).isEqualToIgnoringNanos(expected);
        return this;
    }

    OfferViewAssertion hasItems(int expected) {
        Assertions.assertThat(actual.getItems()).hasSize(expected);
        return this;
    }

    OfferViewAssertion hasItem(UUID expectedTrainingId, BigDecimal expectedPrice) {
        Assertions.assertThat(actual.getItems())
                .anySatisfy(item -> {
                    Assertions.assertThat(item.getTrainingId()).isEqualTo(expectedTrainingId);
                    Assertions.assertThat(item.getPrice()).isEqualByComparingTo(expectedPrice);
                });
        return this;
    }
}
