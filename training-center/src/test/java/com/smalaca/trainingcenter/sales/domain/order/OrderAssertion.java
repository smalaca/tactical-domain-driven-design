package com.smalaca.trainingcenter.sales.domain.order;

import com.smalaca.trainingcenter.sales.domain.money.Money;
import com.smalaca.trainingcenter.sales.domain.offer.OfferId;
import com.smalaca.trainingcenter.sales.domain.training.TrainingId;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

import static org.assertj.core.api.InstanceOfAssertFactories.SET;

public class OrderAssertion extends AbstractAssert<OrderAssertion, Order> {
    private OrderAssertion(Order actual) {
        super(actual, OrderAssertion.class);
    }

    public static OrderAssertion assertThat(Order actual) {
        return new OrderAssertion(actual);
    }

    public OrderAssertion hasOfferId(OfferId expected) {
        Assertions.assertThat(actual).extracting("offerId").isEqualTo(expected);
        return this;
    }

    public OrderAssertion hasItems(int expected) {
        Assertions.assertThat(actual).extracting("items").asInstanceOf(SET).hasSize(expected);
        return this;
    }

    public OrderAssertion hasItem(TrainingId expectedTrainingId, Money expectedPrice) {
        Assertions.assertThat(actual).extracting("items").asInstanceOf(SET).anySatisfy(item -> {
            Assertions.assertThat(item).extracting("trainingId").isEqualTo(expectedTrainingId);
            Assertions.assertThat(item).extracting("price").satisfies(input -> {
                Money itemPrice = (Money) input;
                Assertions.assertThat(itemPrice.amount()).isEqualByComparingTo(expectedPrice.amount());
            });
        });

        return this;
    }

    public OrderAssertion hasOrderId(OrderId expected) {
        Assertions.assertThat(actual).extracting("orderId").isEqualTo(expected);
        return this;
    }
}
