package com.smalaca.trainingcenter.sales.domain.order;

import com.smalaca.annotations.architecture.DomainDrivenDesign;
import com.smalaca.annotations.architecture.PortsAndAdaptersArchitecture;

@DomainDrivenDesign.Repository
@PortsAndAdaptersArchitecture.DrivenPort
public interface OrderRepository {
    Order findBy(OrderId orderId);

    void save(Order order);
}
