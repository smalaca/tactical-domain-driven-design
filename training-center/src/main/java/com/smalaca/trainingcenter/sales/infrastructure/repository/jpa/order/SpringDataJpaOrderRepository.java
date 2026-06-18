package com.smalaca.trainingcenter.sales.infrastructure.repository.jpa.order;

import com.smalaca.annotations.architecture.PortsAndAdaptersArchitecture;
import com.smalaca.trainingcenter.sales.domain.order.Order;
import com.smalaca.trainingcenter.sales.domain.order.OrderId;
import com.smalaca.trainingcenter.sales.domain.order.OrderRepository;
import org.springframework.stereotype.Component;

@PortsAndAdaptersArchitecture.DrivenAdapter
@Component
public class SpringDataJpaOrderRepository implements OrderRepository {
    private final JpaOrderRepository jpaOrderRepository;

    SpringDataJpaOrderRepository(JpaOrderRepository jpaOrderRepository) {
        this.jpaOrderRepository = jpaOrderRepository;
    }

    @Override
    public Order findBy(OrderId orderId) {
        return jpaOrderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }

    @Override
    public void save(Order order) {
        jpaOrderRepository.save(order);
    }
}
