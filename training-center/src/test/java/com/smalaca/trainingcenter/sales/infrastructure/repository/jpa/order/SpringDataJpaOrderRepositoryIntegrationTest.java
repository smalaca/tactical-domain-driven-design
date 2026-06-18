package com.smalaca.trainingcenter.sales.infrastructure.repository.jpa.order;

import com.smalaca.trainingcenter.sales.domain.clock.Clock;
import com.smalaca.trainingcenter.sales.domain.money.Money;
import com.smalaca.trainingcenter.sales.domain.offer.OfferId;
import com.smalaca.trainingcenter.sales.domain.order.Order;
import com.smalaca.trainingcenter.sales.domain.order.OrderId;
import com.smalaca.trainingcenter.sales.domain.order.OrderRepository;
import com.smalaca.trainingcenter.sales.domain.training.TrainingId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.smalaca.trainingcenter.sales.domain.order.OrderAssertion.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@DataJpaTest
@Import(SpringDataJpaOrderRepository.class)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class SpringDataJpaOrderRepositoryIntegrationTest {
    @Autowired private OrderRepository orderRepository;
    @Autowired private JpaOrderRepository jpaOrderRepository;

    private final Clock clock = mock(Clock.class);

    @AfterEach
    void tearDown() {
        jpaOrderRepository.deleteAll();
    }

    @Test
    void shouldThrowExceptionWhenOrderNotFound() {
        OrderId orderId = OrderId.orderId();

        assertThatThrownBy(() -> orderRepository.findBy(orderId))
                .isInstanceOf(OrderNotFoundException.class);
    }

    @Test
    void shouldFindExistingOrder() {
        givenNow();
        OfferId offerId = OfferId.offerId();
        Order order = new Order.Builder(offerId, clock).build();
        orderRepository.save(order);

        Order actual = orderRepository.findBy(order.getOrderId());

        assertThat(actual).hasOfferId(offerId);
    }

    @Test
    void shouldFindSpecificOrderWhenMultipleExist() {
        givenNow();
        OfferId offerId = OfferId.offerId();
        Order order = new Order.Builder(offerId, clock).build();
        orderRepository.save(new Order.Builder(OfferId.offerId(), clock).build());
        orderRepository.save(order);
        orderRepository.save(new Order.Builder(OfferId.offerId(), clock).build());

        Order actual = orderRepository.findBy(order.getOrderId());

        assertThat(actual).hasOfferId(offerId);
    }

    @Test
    void shouldSaveAndLoadOrderWithItems() {
        givenNow();
        OfferId offerId = OfferId.offerId();
        TrainingId trainingId1 = new TrainingId(UUID.randomUUID());
        TrainingId trainingId2 = new TrainingId(UUID.randomUUID());
        Money price1 = new Money(BigDecimal.valueOf(100));
        Money price2 = new Money(BigDecimal.valueOf(200));
        Order order = new Order.Builder(offerId, clock)
                .item(trainingId1, price1)
                .item(trainingId2, price2)
                .build();
        orderRepository.save(order);
        OrderId orderId = order.getOrderId();

        Order actual = orderRepository.findBy(orderId);

        assertThat(actual)
                .hasOrderId(orderId)
                .hasOfferId(offerId)
                .hasItems(2)
                .hasItem(trainingId1, price1)
                .hasItem(trainingId2, price2);
    }

    private void givenNow() {
        LocalDateTime createdAt = LocalDateTime.now();
        given(clock.now()).willReturn(createdAt);
    }
}
