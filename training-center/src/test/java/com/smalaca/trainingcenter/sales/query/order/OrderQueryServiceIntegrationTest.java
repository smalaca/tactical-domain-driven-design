package com.smalaca.trainingcenter.sales.query.order;

import com.smalaca.trainingcenter.sales.domain.clock.Clock;
import com.smalaca.trainingcenter.sales.domain.money.Money;
import com.smalaca.trainingcenter.sales.domain.offer.OfferId;
import com.smalaca.trainingcenter.sales.domain.order.Order;
import com.smalaca.trainingcenter.sales.domain.order.OrderRepository;
import com.smalaca.trainingcenter.sales.domain.training.TrainingId;
import com.smalaca.trainingcenter.sales.infrastructure.repository.jpa.order.SpringDataJpaOrderRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.smalaca.trainingcenter.sales.query.order.OrderViewAssertion.assertThat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@DataJpaTest
@Import({OrderQueryService.class, SpringDataJpaOrderRepository.class})
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class OrderQueryServiceIntegrationTest {
    @Autowired private OrderQueryService queryService;
    @Autowired private JpaOrderViewRepository viewRepository;
    @Autowired private OrderRepository orderRepository;

    private final Clock clock = mock(Clock.class);

    @AfterEach
    void tearDown() {
        viewRepository.deleteAll();
    }

    @Test
    void shouldFindNothingWhenNoOrders() {
        UUID orderId = id();

        Optional<OrderView> actual = queryService.findOneById(orderId);

        assertThat(actual).isEmpty();
    }

    @Test
    void shouldFindOrderById() {
        UUID offerId = id();
        UUID trainingId1 = id();
        BigDecimal price1 = BigDecimal.valueOf(100);
        UUID trainingId2 = id();
        BigDecimal price2 = BigDecimal.valueOf(200);
        LocalDateTime createdAt = LocalDateTime.now().minusMinutes(10);
        UUID orderId = givenOrder(offerId, createdAt, trainingId1, price1, trainingId2, price2);

        Optional<OrderView> actual = queryService.findOneById(orderId);

        assertThat(actual).isPresent();
        assertThat(actual.get())
                .hasOrderId(orderId)
                .hasOfferId(offerId)
                .hasCreationDateTime(createdAt)
                .hasItems(2)
                .hasItem(trainingId1, price1)
                .hasItem(trainingId2, price2);
    }

    @Test
    void shouldFindAllOrders() {
        UUID offerId1 = id();
        LocalDateTime createdAt1 = LocalDateTime.now().minusMinutes(20);
        UUID trainingId1 = id();
        BigDecimal price1 = BigDecimal.valueOf(150);
        UUID orderId1 = givenOrder(offerId1, createdAt1, trainingId1, price1);

        UUID offerId2 = id();
        LocalDateTime createdAt2 = LocalDateTime.now().minusMinutes(10);
        UUID trainingId2 = id();
        BigDecimal price2 = BigDecimal.valueOf(250);
        UUID orderId2 = givenOrder(offerId2, createdAt2, trainingId2, price2);

        List<OrderView> actual = queryService.findAll();

        assertThat(actual).hasSize(2);
        assertThat(actual)
                .anySatisfy(view -> assertThat(view)
                        .hasOrderId(orderId1)
                        .hasOfferId(offerId1)
                        .hasCreationDateTime(createdAt1)
                        .hasItems(1)
                        .hasItem(trainingId1, price1))
                .anySatisfy(view -> assertThat(view)
                        .hasOrderId(orderId2)
                        .hasOfferId(offerId2)
                        .hasCreationDateTime(createdAt2)
                        .hasItems(1)
                        .hasItem(trainingId2, price2));
    }

    private UUID id() {
        return UUID.randomUUID();
    }

    private UUID givenOrder(UUID offerId, LocalDateTime createdAt, Object... trainingIdsAndPrices) {
        given(clock.now()).willReturn(createdAt);
        Order.Builder builder = new Order.Builder(new OfferId(offerId), clock);

        for (int i = 0; i < trainingIdsAndPrices.length; i += 2) {
            UUID trainingId = (UUID) trainingIdsAndPrices[i];
            BigDecimal price = (BigDecimal) trainingIdsAndPrices[i + 1];
            builder.item(new TrainingId(trainingId), new Money(price));
        }

        Order order = builder.build();
        orderRepository.save(order);

        return order.getOrderId().value();
    }
}
