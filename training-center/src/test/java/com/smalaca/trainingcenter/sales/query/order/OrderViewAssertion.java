package com.smalaca.trainingcenter.sales.query.order;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class OrderViewAssertion extends AbstractAssert<OrderViewAssertion, OrderView> {
    private OrderViewAssertion(OrderView actual) {
        super(actual, OrderViewAssertion.class);
    }

    public static OrderViewAssertion assertThat(OrderView actual) {
        return new OrderViewAssertion(actual);
    }

    public OrderViewAssertion hasOrderId(UUID expected) {
        Assertions.assertThat(actual.getOrderId()).isEqualTo(expected);
        return this;
    }

    public OrderViewAssertion hasOfferId(UUID expected) {
        Assertions.assertThat(actual.getOfferId()).isEqualTo(expected);
        return this;
    }

    public OrderViewAssertion hasCreationDateTime(LocalDateTime expected) {
        Assertions.assertThat(actual.getCreatedAt()).isEqualToIgnoringNanos(expected);
        return this;
    }

    public OrderViewAssertion hasItems(int expected) {
        Assertions.assertThat(actual.getItems()).hasSize(expected);
        return this;
    }

    public OrderViewAssertion hasItem(UUID expectedTrainingId, BigDecimal expectedPrice) {
        Assertions.assertThat(actual.getItems())
                .anySatisfy(item -> {
                    Assertions.assertThat(item.getTrainingId()).isEqualTo(expectedTrainingId);
                    Assertions.assertThat(item.getPrice()).isEqualByComparingTo(expectedPrice);
                });
        return this;
    }
}
