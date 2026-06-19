package com.smalaca.trainingcenter.sales.infrastructure.api.rest.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.smalaca.trainingcenter.sales.domain.clock.Clock;
import com.smalaca.trainingcenter.sales.domain.money.Money;
import com.smalaca.trainingcenter.sales.domain.offer.OfferId;
import com.smalaca.trainingcenter.sales.domain.order.Order;
import com.smalaca.trainingcenter.sales.domain.training.TrainingId;
import com.smalaca.trainingcenter.sales.infrastructure.api.rest.client.OrderTestDto;
import com.smalaca.trainingcenter.sales.infrastructure.api.rest.client.TrainingCenterClient;
import com.smalaca.trainingcenter.sales.infrastructure.repository.jpa.order.JpaOrderTestRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.util.Pair;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.smalaca.trainingcenter.sales.infrastructure.api.rest.order.OrderTestDtoAssertion.assertThat;
import static java.util.Arrays.stream;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(JpaOrderTestRepository.class)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class OrderRestControllerTest {
    @Autowired private MockMvc mockMvc;
    @Autowired private JpaOrderTestRepository orderRepository;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private final Clock clock = mock(Clock.class);
    private TrainingCenterClient trainingCenterClient;

    @BeforeEach
    void setUp() {
        trainingCenterClient = new TrainingCenterClient(mockMvc, objectMapper);
    }

    @AfterEach
    void tearDown() {
        orderRepository.deleteAll();
    }

    @Test
    void shouldFindAllOrders() throws Exception {
        UUID trainingId1 = UUID.randomUUID();
        UUID trainingId2 = UUID.randomUUID();
        UUID trainingId3 = UUID.randomUUID();
        UUID offerId1 = UUID.randomUUID();
        UUID offerId2 = UUID.randomUUID();
        UUID orderId1 = existingOrder(offerId1, Pair.of(trainingId1, 10));
        UUID orderId2 = existingOrder(offerId2, Pair.of(trainingId2, 13), Pair.of(trainingId3, 42));

        List<OrderTestDto> actual = trainingCenterClient.orders().findAll();

        assertThat(actual).hasSize(2);
        assertThat(actual)
                .anySatisfy(order -> assertThat(order)
                        .hasOrderId(orderId1)
                        .hasOfferId(offerId1)
                        .hasItems(1)
                        .hasItem(trainingId1, 10))
                .anySatisfy(order -> assertThat(order)
                        .hasOrderId(orderId2)
                        .hasOfferId(offerId2)
                        .hasItems(2)
                        .hasItem(trainingId2, 13)
                        .hasItem(trainingId3, 42));
    }

    @Test
    void shouldFindOrderById() throws Exception {
        UUID offerId = UUID.randomUUID();
        UUID trainingId = UUID.randomUUID();
        UUID orderId = existingOrder(offerId, Pair.of(trainingId, 33));

        OrderTestDto actual = trainingCenterClient.orders().findOne(orderId);

        assertThat(actual)
                .hasOrderId(orderId)
                .hasOfferId(offerId)
                .hasItems(1)
                .hasItem(trainingId, 33);
    }

    @Test
    void shouldReturnNotFoundWhenOrderDoesNotExist() throws Exception {
        UUID nonExistingOrderId = UUID.randomUUID();

        mockMvc.perform(get("/order/" + nonExistingOrderId))
                .andExpect(status().isNotFound());
    }

    private UUID existingOrder(UUID offerId, Pair<UUID, Integer>... trainings) {
        given(clock.now()).willReturn(LocalDateTime.now());
        Order.Builder builder = new Order.Builder(new OfferId(offerId), clock);
        stream(trainings).forEach(training -> {
            builder.item(new TrainingId(training.getFirst()), new Money(BigDecimal.valueOf(training.getSecond())));
        });

        Order order = builder.build();
        orderRepository.save(order);

        return order.getOrderId().value();
    }
}
