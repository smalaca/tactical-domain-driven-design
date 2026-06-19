package com.smalaca.trainingcenter.sales.infrastructure.api.rest.order;

import com.smalaca.trainingcenter.sales.infrastructure.api.rest.client.OrderTestDto;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

import java.math.BigDecimal;
import java.util.UUID;

public class OrderTestDtoAssertion extends AbstractAssert<OrderTestDtoAssertion, OrderTestDto> {
    private OrderTestDtoAssertion(OrderTestDto actual) {
        super(actual, OrderTestDtoAssertion.class);
    }

    public static OrderTestDtoAssertion assertThat(OrderTestDto actual) {
        return new OrderTestDtoAssertion(actual);
    }

    public OrderTestDtoAssertion hasOrderId(UUID expected) {
        Assertions.assertThat(actual.orderId()).isEqualTo(expected);
        return this;
    }

    public OrderTestDtoAssertion hasOfferId(UUID expected) {
        Assertions.assertThat(actual.offerId()).isEqualTo(expected);
        return this;
    }

    public OrderTestDtoAssertion hasItems(int expected) {
        Assertions.assertThat(actual.items()).hasSize(expected);
        return this;
    }

    public OrderTestDtoAssertion hasItem(UUID expectedTrainingId, int expectedPrice) {
        Assertions.assertThat(actual.items())
                .anySatisfy(item -> {
                    Assertions.assertThat(item.trainingId()).isEqualTo(expectedTrainingId);
                    Assertions.assertThat(item.price()).isEqualByComparingTo(BigDecimal.valueOf(expectedPrice));
                });
        return this;
    }
}
