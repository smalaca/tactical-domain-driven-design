package com.smalaca.trainingcenter.sales.infrastructure.repository.jpa.order;

import com.smalaca.trainingcenter.sales.domain.order.Order;
import com.smalaca.trainingcenter.sales.domain.order.OrderId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
interface JpaOrderRepository extends CrudRepository<Order, OrderId> {
}
