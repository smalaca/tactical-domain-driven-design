package com.smalaca.trainingcenter.sales.domain.offer;

import com.smalaca.trainingcenter.sales.domain.cart.CartId;
import com.smalaca.trainingcenter.sales.domain.training.TrainingId;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

import java.time.LocalDateTime;
import java.util.Set;

public class OfferAssertion extends AbstractAssert<OfferAssertion, Offer> {
    private OfferAssertion(Offer actual) {
        super(actual, OfferAssertion.class);
    }

    public static OfferAssertion assertThat(Offer actual) {
        return new OfferAssertion(actual);
    }

    public OfferAssertion hasCartId(CartId expected) {
        Assertions.assertThat(actual).extracting("cartId").isEqualTo(expected);
        return this;
    }

    public OfferAssertion hasItems(int expected) {
        Assertions.assertThat(actual).extracting("items").satisfies(items -> {
            Set<?> itemsSet = (Set<?>) items;
            Assertions.assertThat(itemsSet).hasSize(expected);
        });
        return this;
    }

    public OfferAssertion hasItem(TrainingId expectedTrainingId, Money expectedPrice) {
        Assertions.assertThat(actual).extracting("items").satisfies(items -> {
            Set<?> itemsSet = (Set<?>) items;

            Assertions.assertThat(itemsSet).anySatisfy(item -> {
                Assertions.assertThat(item).extracting("trainingId").isEqualTo(expectedTrainingId);
                Assertions.assertThat(item).extracting("price").isEqualTo(expectedPrice);
            });
        });

        return this;
    }

    public OfferAssertion hasOfferId() {
        Assertions.assertThat(actual).extracting("offerId").isNotNull();
        return this;
    }

    public OfferAssertion hasCreatedAt(LocalDateTime expected) {
        Assertions.assertThat(actual).extracting("createdAt").isEqualTo(expected);
        return this;
    }

    public OfferAssertion hasValidTo(LocalDateTime expected) {
        Assertions.assertThat(actual).extracting("validTo").isEqualTo(expected);
        return this;
    }
}
