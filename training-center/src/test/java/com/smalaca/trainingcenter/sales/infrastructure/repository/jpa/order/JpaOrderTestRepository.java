package com.smalaca.trainingcenter.sales.infrastructure.repository.jpa.order;

import com.smalaca.trainingcenter.sales.domain.order.Order;
import com.smalaca.trainingcenter.sales.domain.order.OrderId;

import java.util.Optional;
import java.util.UUID;

public class JpaOrderTestRepository {
    private final JpaOrderRepository jpaOrderRepository;

    public JpaOrderTestRepository(JpaOrderRepository jpaOrderRepository) {
        this.jpaOrderRepository = jpaOrderRepository;
    }

    public Optional<Order> findById(UUID orderId) {
        return jpaOrderRepository.findById(new OrderId(orderId));
    }

    public void save(Order order) {
        jpaOrderRepository.save(order);
    }

    public void deleteAll() {
        jpaOrderRepository.deleteAll();
    }
}
